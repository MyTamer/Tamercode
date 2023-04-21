package com.android.vending.licensing;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.android.vending.licensing.LicenseCheckerCallback.ApplicationErrorCode;
import com.android.vending.licensing.Policy.LicenseResponse;
import com.android.vending.licensing.util.Base64;
import com.android.vending.licensing.util.Base64DecoderException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * Client library for Android Market license verifications.
 * <p>
 * The LicenseChecker is configured via a {@link Policy} which contains the
 * logic to determine whether a user should have access to the application.
 * For example, the Policy can define a threshold for allowable number of
 * server or client failures before the library reports the user as not having
 * access.
 * <p>
 * Must also provide the Base64-encoded RSA public key associated with your
 * developer account. The public key is obtainable from the publisher site.
 */
public class LicenseChecker implements ServiceConnection {

    private static final String TAG = "LicenseChecker";

    private static final String KEY_FACTORY_ALGORITHM = "RSA";

    private static final int TIMEOUT_MS = 10 * 1000;

    private static final SecureRandom RANDOM = new SecureRandom();

    private ILicensingService mService;

    private PublicKey mPublicKey;

    private final Context mContext;

    private final Policy mPolicy;

    /**
     * A handler for running tasks on a background thread. We don't want
     * license processing to block the UI thread.
     */
    private Handler mHandler;

    private final String mPackageName;

    private final String mVersionCode;

    private final Set<LicenseValidator> mChecksInProgress = new HashSet<LicenseValidator>();

    private final Queue<LicenseValidator> mPendingChecks = new LinkedList<LicenseValidator>();

    /**
     * @param context a Context
     * @param policy implementation of Policy
     * @param encodedPublicKey Base64-encoded RSA public key
     * @throws IllegalArgumentException if encodedPublicKey is invalid
     */
    public LicenseChecker(Context context, Policy policy, String encodedPublicKey) {
        mContext = context;
        mPolicy = policy;
        mPublicKey = generatePublicKey(encodedPublicKey);
        mPackageName = mContext.getPackageName();
        mVersionCode = getVersionCode(context, mPackageName);
        HandlerThread handlerThread = new HandlerThread("background thread");
        handlerThread.start();
        mHandler = new Handler(handlerThread.getLooper());
    }

    /**
     * Generates a PublicKey instance from a string containing the
     * Base64-encoded public key.
     *
     * @param encodedPublicKey Base64-encoded public key
     * @throws IllegalArgumentException if encodedPublicKey is invalid
     */
    private static PublicKey generatePublicKey(String encodedPublicKey) {
        try {
            byte[] decodedKey = Base64.decode(encodedPublicKey);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_FACTORY_ALGORITHM);
            return keyFactory.generatePublic(new X509EncodedKeySpec(decodedKey));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (Base64DecoderException e) {
            Log.e(TAG, "Could not decode from Base64.");
            throw new IllegalArgumentException(e);
        } catch (InvalidKeySpecException e) {
            Log.e(TAG, "Invalid key specification.");
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Checks if the user should have access to the app.
     *
     * @param callback
     */
    public synchronized void checkAccess(LicenseCheckerCallback callback) {
        if (mPolicy.allowAccess()) {
            Log.i(TAG, "Using cached license response");
            callback.allow();
        } else {
            LicenseValidator validator = new LicenseValidator(mPolicy, new NullDeviceLimiter(), callback, generateNonce(), mPackageName, mVersionCode);
            if (mService == null) {
                Log.i(TAG, "Binding to licensing service.");
                try {
                    boolean bindResult = mContext.bindService(new Intent(ILicensingService.class.getName()), this, Context.BIND_AUTO_CREATE);
                    if (bindResult) {
                        mPendingChecks.offer(validator);
                    } else {
                        Log.e(TAG, "Could not bind to service.");
                        handleServiceConnectionError(validator);
                    }
                } catch (SecurityException e) {
                    callback.applicationError(ApplicationErrorCode.MISSING_PERMISSION);
                }
            } else {
                mPendingChecks.offer(validator);
                runChecks();
            }
        }
    }

    private void runChecks() {
        LicenseValidator validator;
        while ((validator = mPendingChecks.poll()) != null) {
            try {
                Log.i(TAG, "Calling checkLicense on service for " + validator.getPackageName());
                mService.checkLicense(validator.getNonce(), validator.getPackageName(), new ResultListener(validator));
                mChecksInProgress.add(validator);
            } catch (RemoteException e) {
                Log.w(TAG, "RemoteException in checkLicense call.", e);
                handleServiceConnectionError(validator);
            }
        }
    }

    private synchronized void finishCheck(LicenseValidator validator) {
        mChecksInProgress.remove(validator);
        if (mChecksInProgress.isEmpty()) {
            cleanupService();
        }
    }

    private class ResultListener extends ILicenseResultListener.Stub {

        private final LicenseValidator mValidator;

        private Runnable mOnTimeout;

        public ResultListener(LicenseValidator validator) {
            mValidator = validator;
            mOnTimeout = new Runnable() {

                public void run() {
                    Log.i(TAG, "Check timed out.");
                    handleServiceConnectionError(mValidator);
                    finishCheck(mValidator);
                }
            };
            startTimeout();
        }

        public void verifyLicense(final int responseCode, final String signedData, final String signature) {
            mHandler.post(new Runnable() {

                public void run() {
                    Log.i(TAG, "Received response.");
                    if (mChecksInProgress.contains(mValidator)) {
                        clearTimeout();
                        mValidator.verify(mPublicKey, responseCode, signedData, signature);
                        finishCheck(mValidator);
                    }
                }
            });
        }

        private void startTimeout() {
            Log.i(TAG, "Start monitoring timeout.");
            mHandler.postDelayed(mOnTimeout, TIMEOUT_MS);
        }

        private void clearTimeout() {
            Log.i(TAG, "Clearing timeout.");
            mHandler.removeCallbacks(mOnTimeout);
        }
    }

    public synchronized void onServiceConnected(ComponentName name, IBinder service) {
        mService = ILicensingService.Stub.asInterface(service);
        runChecks();
    }

    public synchronized void onServiceDisconnected(ComponentName name) {
        Log.w(TAG, "Service unexpectedly disconnected.");
        mService = null;
    }

    /**
     * Generates policy response for service connection errors, as a result of
     * disconnections or timeouts.
     */
    private synchronized void handleServiceConnectionError(LicenseValidator validator) {
        mPolicy.processServerResponse(LicenseResponse.RETRY, null);
        if (mPolicy.allowAccess()) {
            validator.getCallback().allow();
        } else {
            validator.getCallback().dontAllow();
        }
    }

    /** Unbinds service if necessary and removes reference to it. */
    private void cleanupService() {
        if (mService != null) {
            try {
                mContext.unbindService(this);
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "Unable to unbind from licensing service (already unbound)");
            }
            mService = null;
        }
    }

    /**
     * Inform the library that the context is about to be destroyed, so that
     * any open connections can be cleaned up.
     * <p>
     * Failure to call this method can result in a crash under certain
     * circumstances, such as during screen rotation if an Activity requests
     * the license check or when the user exits the application.
     */
    public synchronized void onDestroy() {
        cleanupService();
        mHandler.getLooper().quit();
    }

    /** Generates a nonce (number used once). */
    private int generateNonce() {
        return RANDOM.nextInt();
    }

    /**
     * Get version code for the application package name.
     *
     * @param context
     * @param packageName application package name
     * @return the version code or empty string if package not found
     */
    private static String getVersionCode(Context context, String packageName) {
        try {
            return String.valueOf(context.getPackageManager().getPackageInfo(packageName, 0).versionCode);
        } catch (NameNotFoundException e) {
            Log.e(TAG, "Package not found. could not get version code.");
            return "";
        }
    }
}