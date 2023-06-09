package de.cachebox_test.Views.Forms;

import java.util.Timer;
import java.util.TimerTask;
import CB_Core.GlobalCore;
import CB_Core.Log.Logger;
import CB_Core.Map.Descriptor;
import CB_Core.Map.Descriptor.PointD;
import CB_Core.Types.Coordinate;
import CB_Core.Types.MeasuredCoord;
import CB_Core.Types.MeasuredCoordList;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.cachebox_test.R;
import de.cachebox_test.main;
import de.cachebox_test.Custom_Controls.CanvasDrawControl;
import de.cachebox_test.Ui.ActivityUtils;

public class MeasureCoordinateActivity extends Activity implements LocationListener {

    private Intent aktIntent;

    public static LinearLayout strengthLayout;

    private MeasuredCoordList mMeasureList = new MeasuredCoordList();

    private TextView lblMeasureCount;

    private TextView lblMeasureCoord;

    private TextView lblDescMeasureCount;

    private TextView lblDescMeasureCoord;

    private int MeasureCount = 0;

    CanvasDrawControl panelPreview;

    /**
	 * Die MeasureCoordinate Activity hat ihren eigenen locationmanager, mit einem H�heren Aktualisierungs Intewall
	 */
    public static LocationManager locationManager;

    public void onCreate(Bundle savedInstanceState) {
        ActivityUtils.onActivityCreateSetTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.measure_coordinate);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Bundle bundle = getIntent().getExtras();
        MeasuredCoord.Referenz = (Coordinate) bundle.getSerializable("Coord");
        if (MeasuredCoord.Referenz == null) {
            MeasuredCoord.Referenz = GlobalCore.LastValidPosition;
        }
        if (MeasuredCoord.Referenz == null) {
            MeasuredCoord.Referenz = new Coordinate();
        }
        aktIntent = getIntent();
        findViewById();
        Button bOK = (Button) findViewById(R.id.edco_ok);
        bOK.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                aktIntent.putExtra("SOMETHING", "EXTRAS");
                Bundle extras = new Bundle();
                extras.putSerializable("CoordResult", mMeasureList.getAccuWeightedAverageCoord());
                aktIntent.putExtras(extras);
                setResult(RESULT_OK, aktIntent);
                finish();
            }
        });
        Button bCancel = (Button) findViewById(R.id.edco_cancel);
        bCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                aktIntent.putExtra("SOMETHING", "EXTRAS");
                Bundle extras = new Bundle();
                extras.putSerializable("CoordResult", null);
                aktIntent.putExtras(extras);
                setResult(RESULT_OK, aktIntent);
                finish();
            }
        });
        setSatStrength();
        bOK.setText(GlobalCore.Translations.Get("ok"));
        bCancel.setText(GlobalCore.Translations.Get("cancel"));
        lblDescMeasureCoord.setText(GlobalCore.Translations.Get("MeasureCoord"));
        lblDescMeasureCount.setText(GlobalCore.Translations.Get("MeasureCount"));
        timer.schedule(task, 1200);
    }

    final Timer timer = new Timer();

    final TimerTask task = new TimerTask() {

        @Override
        public void run() {
            try {
                Thread t = new Thread() {

                    public void run() {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                if (!inRepaint) repaintPreview();
                            }
                        });
                    }
                };
                t.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    private void findViewById() {
        panelPreview = (CanvasDrawControl) findViewById(R.id.meco_panel);
        strengthLayout = (LinearLayout) findViewById(R.id.strength_control);
        lblMeasureCount = (TextView) findViewById(R.id.textView2);
        lblMeasureCoord = (TextView) findViewById(R.id.textView4);
        lblDescMeasureCount = (TextView) findViewById(R.id.textView1);
        lblDescMeasureCoord = (TextView) findViewById(R.id.textView3);
    }

    private static View[] balken = null;

    public static void setSatStrength() {
        de.cachebox_test.Locator.GPS.setSatStrength(strengthLayout, balken);
    }

    private void initialLocationManager() {
        try {
            if (locationManager != null) {
                locationManager.removeUpdates(this);
                locationManager = null;
            }
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setCostAllowed(true);
            criteria.setPowerRequirement(Criteria.POWER_HIGH);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        } catch (Exception e) {
            Logger.Error("MesureCoordinate.initialLocationManager()", "", e);
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Locator_LocationDataReceived(location);
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    protected void onResume() {
        MeasureCount = 0;
        if (mMeasureList != null) {
            mMeasureList.clear();
        } else {
            mMeasureList = new MeasuredCoordList();
        }
        initialLocationManager();
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        locationManager.removeUpdates(this);
        mMeasureList = null;
        super.onDestroy();
    }

    final int projectionZoom = 18;

    final double metersPerTile = 6378137.0 / Math.pow(2, projectionZoom);

    Canvas graphics;

    void Locator_LocationDataReceived(Location location) {
        if (mMeasureList == null) {
            main.Toast("MeasureList = null");
            return;
        }
        Coordinate coord = new Coordinate(location.getLatitude(), location.getLongitude());
        if (MeasureCount == 0) lblMeasureCoord.setText("");
        MeasureCount++;
        mMeasureList.add(new MeasuredCoord(location.getLatitude(), location.getLongitude(), location.getAccuracy()));
        lblMeasureCount.setText(String.valueOf(MeasureCount) + "/" + String.valueOf(mMeasureList.size()));
        if (mMeasureList.size() % 10 == 0) {
            mMeasureList.setAverage();
            mMeasureList.clearDiscordantValue();
            lblMeasureCoord.setText(mMeasureList.toString());
        }
        repaintPreview();
    }

    Paint redPen = new Paint(Color.RED);

    Paint blackPen = new Paint(Color.BLACK);

    Paint blueBrush = new Paint(Color.BLUE);

    private boolean inRepaint = false;

    void repaintPreview() {
        inRepaint = true;
        if (redPen == null) {
            redPen = new Paint();
            blackPen = new Paint();
            blackPen.setColor(Color.BLACK);
            blackPen.setAntiAlias(true);
            blackPen.setStyle(Style.STROKE);
            blackPen.setStrokeWidth(2);
            blueBrush = new Paint();
        }
        setSatStrength();
        Canvas graphics = panelPreview.getCanvas();
        graphics.drawColor(Color.LTGRAY);
        int centerX = panelPreview.getWidth() / 2;
        int centerY = panelPreview.getHeight() / 2;
        int minPix = Math.min(panelPreview.getWidth(), panelPreview.getHeight());
        if (mMeasureList.size() > 0) {
            double medianLat = MeasuredCoord.Referenz.Latitude;
            double medianLon = MeasuredCoord.Referenz.Longitude;
            MeasuredCoordList sortetdList = (MeasuredCoordList) mMeasureList.clone();
            sortetdList.sort();
            double peakLat = Math.max(Math.abs(sortetdList.get(0).getLatitude() - medianLat), Math.abs(sortetdList.get(sortetdList.size() - 1).getLatitude() - medianLat));
            double peakLon = Math.max(Math.abs(sortetdList.get(0).getLongitude() - medianLon), Math.abs(sortetdList.get(sortetdList.size() - 1).getLongitude() - medianLon));
            double medianX = Descriptor.LongitudeToTileX(projectionZoom, medianLon);
            double medianY = Descriptor.LatitudeToTileY(projectionZoom, medianLat);
            double extremeX = Descriptor.LongitudeToTileX(projectionZoom, peakLon + medianLon);
            double extremeY = Descriptor.LatitudeToTileY(projectionZoom, peakLat + medianLat);
            double peakX = Math.abs(extremeX - medianX);
            double peakY = Math.abs(extremeY - medianY);
            double maxPeak = Math.max(peakX, peakY);
            double factor = (maxPeak > 0) ? (double) minPix / maxPeak : 1;
            int x = centerX;
            int y = centerY;
            for (int i = 1; i < mMeasureList.size(); i++) {
                PointD lastDrawEntry = Descriptor.projectCoordinate(mMeasureList.get(i - 1).getLatitude(), mMeasureList.get(i - 1).getLongitude(), projectionZoom);
                int lastX = (int) (centerX + (lastDrawEntry.X - medianX) * factor);
                int lastY = (int) (centerY - (lastDrawEntry.Y - medianY) * factor);
                PointD thisDrawEntry = Descriptor.projectCoordinate(mMeasureList.get(i).getLatitude(), mMeasureList.get(i).getLongitude(), projectionZoom);
                x = (int) (centerX + (thisDrawEntry.X - medianX) * factor);
                y = (int) (centerY - (thisDrawEntry.Y - medianY) * factor);
                redPen.setColor(Color.RED);
                redPen.setAntiAlias(true);
                redPen.setStrokeWidth(2);
                graphics.drawLine(lastX, lastY, x, y, redPen);
            }
            blueBrush.setColor(Color.BLUE);
            blueBrush.setAntiAlias(true);
            blueBrush.setStrokeWidth(2);
            graphics.drawCircle(x, y, 4, blueBrush);
        }
        int m2 = (int) ((4 * minPix) / metersPerTile);
        int m4 = m2 * 2;
        blackPen.setStrokeWidth(1.5f);
        blackPen.setStyle(Paint.Style.STROKE);
        blackPen.setAntiAlias(true);
        graphics.drawCircle(centerX, centerY, m2, blackPen);
        graphics.drawCircle(centerX, centerY, m4, blackPen);
        graphics.drawLine(centerX, 0, centerX, panelPreview.getHeight(), blackPen);
        graphics.drawLine(0, centerY, panelPreview.getWidth(), centerY, blackPen);
        panelPreview.invalidate();
        inRepaint = false;
    }
}
