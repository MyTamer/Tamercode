package com.android.settings;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;
import android.telephony.TelephonyManager;

public class Settings extends PreferenceActivity {

    private static final String KEY_PARENT = "parent";

    private static final String KEY_CALL_SETTINGS = "call_settings";

    private static final String KEY_SYNC_SETTINGS = "sync_settings";

    private static final String KEY_SEARCH_SETTINGS = "search_settings";

    private static final String KEY_DOCK_SETTINGS = "dock_settings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        int activePhoneType = TelephonyManager.getDefault().getPhoneType();
        PreferenceGroup parent = (PreferenceGroup) findPreference(KEY_PARENT);
        Utils.updatePreferenceToSpecificActivityOrRemove(this, parent, KEY_SYNC_SETTINGS, 0);
        Utils.updatePreferenceToSpecificActivityOrRemove(this, parent, KEY_SEARCH_SETTINGS, 0);
        Preference dockSettings = parent.findPreference(KEY_DOCK_SETTINGS);
        if (getResources().getBoolean(R.bool.has_dock_settings) == false && dockSettings != null) {
            parent.removePreference(dockSettings);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        findPreference(KEY_CALL_SETTINGS).setEnabled(!AirplaneModeEnabler.isAirplaneModeOn(this));
    }
}
