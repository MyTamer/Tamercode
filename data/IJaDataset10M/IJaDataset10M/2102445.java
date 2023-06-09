package com.android.settings.bluetooth;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import com.android.settings.R;
import com.android.settings.bluetooth.LocalBluetoothProfileManager.Profile;

/**
 * BluetoothEventRedirector receives broadcasts and callbacks from the Bluetooth
 * API and dispatches the event on the UI thread to the right class in the
 * Settings.
 */
public class BluetoothEventRedirector {

    private static final String TAG = "BluetoothEventRedirector";

    private LocalBluetoothManager mManager;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.v(TAG, "Received " + intent.getAction());
            String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                mManager.setBluetoothStateInt(state);
            } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED)) {
                mManager.onScanningStateChanged(true);
            } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                mManager.onScanningStateChanged(false);
            } else if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                short rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
                BluetoothClass btClass = intent.getParcelableExtra(BluetoothDevice.EXTRA_CLASS);
                String name = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);
                mManager.getCachedDeviceManager().onDeviceAppeared(device, rssi, btClass, name);
            } else if (action.equals(BluetoothDevice.ACTION_DISAPPEARED)) {
                mManager.getCachedDeviceManager().onDeviceDisappeared(device);
            } else if (action.equals(BluetoothDevice.ACTION_NAME_CHANGED)) {
                mManager.getCachedDeviceManager().onDeviceNameUpdated(device);
            } else if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                int bondState = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
                CachedBluetoothDeviceManager cachedDeviceMgr = mManager.getCachedDeviceManager();
                cachedDeviceMgr.onBondingStateChanged(device, bondState);
                if (bondState == BluetoothDevice.BOND_NONE) {
                    if (device.isBluetoothDock()) {
                        mManager.removeDockAutoConnectSetting(device.getAddress());
                        if (!device.getAddress().equals(getDockedDeviceAddress(context))) {
                            cachedDeviceMgr.onDeviceDisappeared(device);
                        }
                    }
                    int reason = intent.getIntExtra(BluetoothDevice.EXTRA_REASON, BluetoothDevice.ERROR);
                    cachedDeviceMgr.showUnbondMessage(device, reason);
                }
            } else if (action.equals(BluetoothHeadset.ACTION_STATE_CHANGED)) {
                int newState = intent.getIntExtra(BluetoothHeadset.EXTRA_STATE, 0);
                int oldState = intent.getIntExtra(BluetoothHeadset.EXTRA_PREVIOUS_STATE, 0);
                if (newState == BluetoothHeadset.STATE_DISCONNECTED && oldState == BluetoothHeadset.STATE_CONNECTING) {
                    Log.i(TAG, "Failed to connect BT headset");
                }
                mManager.getCachedDeviceManager().onProfileStateChanged(device, Profile.HEADSET, newState);
            } else if (action.equals(BluetoothA2dp.ACTION_SINK_STATE_CHANGED)) {
                int newState = intent.getIntExtra(BluetoothA2dp.EXTRA_SINK_STATE, 0);
                int oldState = intent.getIntExtra(BluetoothA2dp.EXTRA_PREVIOUS_SINK_STATE, 0);
                if (newState == BluetoothA2dp.STATE_DISCONNECTED && oldState == BluetoothA2dp.STATE_CONNECTING) {
                    Log.i(TAG, "Failed to connect BT A2DP");
                }
                mManager.getCachedDeviceManager().onProfileStateChanged(device, Profile.A2DP, newState);
            } else if (action.equals(BluetoothDevice.ACTION_CLASS_CHANGED)) {
                mManager.getCachedDeviceManager().onBtClassChanged(device);
            } else if (action.equals(BluetoothDevice.ACTION_UUID)) {
                mManager.getCachedDeviceManager().onUuidChanged(device);
            } else if (action.equals(BluetoothDevice.ACTION_PAIRING_CANCEL)) {
                int errorMsg = R.string.bluetooth_pairing_error_message;
                mManager.showError(device, R.string.bluetooth_error_title, errorMsg);
            } else if (action.equals(Intent.ACTION_DOCK_EVENT)) {
                int anythingButUnDocked = Intent.EXTRA_DOCK_STATE_UNDOCKED + 1;
                int state = intent.getIntExtra(Intent.EXTRA_DOCK_STATE, anythingButUnDocked);
                if (state == Intent.EXTRA_DOCK_STATE_UNDOCKED) {
                    if (device != null && device.getBondState() == BluetoothDevice.BOND_NONE) {
                        mManager.getCachedDeviceManager().onDeviceDisappeared(device);
                    }
                }
            }
        }
    };

    public BluetoothEventRedirector(LocalBluetoothManager localBluetoothManager) {
        mManager = localBluetoothManager;
    }

    public void start() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothDevice.ACTION_DISAPPEARED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothDevice.ACTION_NAME_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_PAIRING_CANCEL);
        filter.addAction(BluetoothA2dp.ACTION_SINK_STATE_CHANGED);
        filter.addAction(BluetoothHeadset.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_CLASS_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_UUID);
        filter.addAction(Intent.ACTION_DOCK_EVENT);
        mManager.getContext().registerReceiver(mBroadcastReceiver, filter);
    }

    public void stop() {
        mManager.getContext().unregisterReceiver(mBroadcastReceiver);
    }

    private String getDockedDeviceAddress(Context context) {
        Intent i = context.registerReceiver(null, new IntentFilter(Intent.ACTION_DOCK_EVENT));
        if (i != null) {
            int state = i.getIntExtra(Intent.EXTRA_DOCK_STATE, Intent.EXTRA_DOCK_STATE_UNDOCKED);
            if (state != Intent.EXTRA_DOCK_STATE_UNDOCKED) {
                BluetoothDevice device = i.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device != null) {
                    return device.getAddress();
                }
            }
        }
        return null;
    }
}
