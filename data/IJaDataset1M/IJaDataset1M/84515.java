package org.andnav.osm;

import org.andnav.osm.contributor.OSMUploader;
import org.andnav.osm.contributor.RouteRecorder;
import org.andnav.osm.util.constants.OpenStreetMapConstants;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Baseclass for Activities who want to contribute to the OpenStreetMap Project.
 * @author Nicolas Gramlich
 *
 */
public abstract class OpenStreetMapActivity extends Activity implements OpenStreetMapConstants {

    protected static final String PROVIDER_NAME = LocationManager.GPS_PROVIDER;

    protected SampleLocationListener mLocationListener;

    protected RouteRecorder mRouteRecorder = new RouteRecorder();

    protected boolean mDoGPSRecordingAndContributing;

    protected LocationManager mLocationManager;

    public int mNumSatellites = NOT_SET;

    /**
	 * Calls <code>onCreate(final Bundle savedInstanceState, final boolean pDoGPSRecordingAndContributing)</code> with <code>pDoGPSRecordingAndContributing == true</code>.<br/>
	 * That means it automatically contributes to the OpenStreetMap Project in the background.
	 * @param savedInstanceState
	 */
    public void onCreate(final Bundle savedInstanceState) {
        onCreate(savedInstanceState, true);
    }

    /**
	 * Called when the activity is first created. Registers LocationListener.
	 * @param savedInstanceState
	 * @param pDoGPSRecordingAndContributing If <code>true</code>, it automatically contributes to the OpenStreetMap Project in the background.
	 */
    public void onCreate(final Bundle savedInstanceState, final boolean pDoGPSRecordingAndContributing) {
        super.onCreate(savedInstanceState);
        if (pDoGPSRecordingAndContributing) this.enableDoGPSRecordingAndContributing(); else this.disableDoGPSRecordingAndContributing(false);
        initLocation();
    }

    private LocationManager getLocationManager() {
        if (this.mLocationManager == null) this.mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return this.mLocationManager;
    }

    private void initLocation() {
        this.mLocationListener = new SampleLocationListener();
        getLocationManager().requestLocationUpdates(PROVIDER_NAME, 2000, 20, this.mLocationListener);
    }

    public abstract void onLocationLost();

    public abstract void onLocationChanged(final Location pLoc);

    /**
	 * Called when activity is destroyed. Unregisters LocationListener.
	 */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        getLocationManager().removeUpdates(mLocationListener);
        if (this.mDoGPSRecordingAndContributing) {
            OSMUploader.uploadAsync(this.mRouteRecorder.getRecordedGeoPoints());
        }
    }

    public void enableDoGPSRecordingAndContributing() {
        if (this.mDoGPSRecordingAndContributing) return;
        this.mRouteRecorder = new RouteRecorder();
        this.mDoGPSRecordingAndContributing = true;
    }

    public void disableDoGPSRecordingAndContributing(final boolean pContributdeCurrentRoute) {
        if (!this.mDoGPSRecordingAndContributing) return;
        if (pContributdeCurrentRoute) {
            OSMUploader.uploadAsync(this.mRouteRecorder.getRecordedGeoPoints());
        }
        this.mRouteRecorder = null;
        this.mDoGPSRecordingAndContributing = false;
    }

    /**
	 * Logs all Location-changes to <code>mRouteRecorder</code>.
	 * @author plusminus
	 */
    private class SampleLocationListener implements LocationListener {

        public void onLocationChanged(final Location loc) {
            if (loc != null) {
                if (OpenStreetMapActivity.this.mDoGPSRecordingAndContributing) OpenStreetMapActivity.this.mRouteRecorder.add(loc, OpenStreetMapActivity.this.mNumSatellites);
                OpenStreetMapActivity.this.onLocationChanged(loc);
            } else {
                OpenStreetMapActivity.this.onLocationLost();
            }
        }

        public void onStatusChanged(String a, int i, Bundle b) {
            OpenStreetMapActivity.this.mNumSatellites = b.getInt("satellites", NOT_SET);
        }

        public void onProviderEnabled(String a) {
        }

        public void onProviderDisabled(String a) {
        }
    }
}
