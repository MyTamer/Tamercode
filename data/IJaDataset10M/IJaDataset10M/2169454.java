package com.limegroup.gnutella.util;

import org.limewire.service.ErrorService;
import org.limewire.util.CommonUtils;
import org.limewire.util.OSUtils;

/**
 * A collection of utility methods for OSX.
 * These methods should only be called if run from OSX,
 * otherwise ClassNotFoundErrors may occur.
 *
 * To determine if the Cocoa Foundation classes are present,
 * use the method CommonUtils.isCocoaFoundationAvailable().
 */
public class MacOSXUtils {

    static {
        if (OSUtils.isMacOSX105() || OSUtils.isMacOSX106()) {
            try {
                System.loadLibrary("MacOSXUtilsLeopard");
            } catch (UnsatisfiedLinkError err) {
                ErrorService.error(err);
            }
        } else if (OSUtils.isAnyMac()) {
            try {
                System.loadLibrary("MacOSXUtilsTiger");
            } catch (UnsatisfiedLinkError err) {
                ErrorService.error(err);
            }
        }
    }

    private MacOSXUtils() {
    }

    /**
     * The name of the app that launches.
     */
    private static final String APP_NAME = "FrostWire.app";

    /**
     * Modifies the loginwindow.plist file to either include or exclude
     * starting up FrostWire.
     */
    public static void setLoginStatus(boolean allow) {
        SetLoginStatusNative(allow);
    }

    /**
     * Gets the full user's name.
     */
    public static String getUserName() {
        return GetCurrentFullUserName();
    }

    /**
     * Retrieves the app directory & name.
     * If the user is not running from the bundled app as we named it,
     * defaults to /Applications/FrostWire/ as the directory of the app.
     */
    public static String getAppDir() {
        String appDir = "/Applications/FrostWire/";
        String path = CommonUtils.getCurrentDirectory().getPath();
        int app = path.indexOf("FrostWire.app");
        if (app != -1) appDir = path.substring(0, app);
        return appDir + APP_NAME;
    }

    /**
     * Gets the full user's name.
     */
    private static final native String GetCurrentFullUserName();

    /**
     * [Un]registers FrostWire from the startup items list.
     */
    private static final native void SetLoginStatusNative(boolean allow);
}
