package lejos.android;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommLogListener;
import lejos.pc.comm.NXTConnector;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LeJOSDroid extends Activity {

    public static enum CONN_TYPE {

        LEJOS_PACKET, LEGO_LCP
    }

    class UIMessageHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case MESSAGE:
                    _message.setText((String) msg.getData().get(MESSAGE_CONTENT));
                    break;
                case TOAST:
                    showToast((String) msg.getData().get(MESSAGE_CONTENT));
                    break;
            }
            _message.setVisibility(View.VISIBLE);
            _message.requestLayout();
        }
    }

    public static final String MESSAGE_CONTENT = "String_message";

    public static final int MESSAGE = 1000;

    public static final int TOAST = 2000;

    private BTSend btSend;

    private TachoCount tachoCount;

    private Toast reusableToast;

    private TextView _message;

    private static final String GO_AHEAD = "Choose one!";

    public static UIMessageHandler mUIMessageHandler;

    private static final String TAG = "LeJOSDroid";

    public static NXTConnector connect(final CONN_TYPE connection_type) {
        Log.d(TAG, " about to add LEJOS listener ");
        NXTConnector conn = new NXTConnector();
        conn.setDebug(true);
        conn.addLogListener(new NXTCommLogListener() {

            public void logEvent(String arg0) {
                Log.e(TAG + " NXJ log:", arg0);
            }

            public void logEvent(Throwable arg0) {
                Log.e(TAG + " NXJ log:", arg0.getMessage(), arg0);
            }
        });
        switch(connection_type) {
            case LEGO_LCP:
                conn.connectTo("btspp://NXT", NXTComm.LCP);
                break;
            case LEJOS_PACKET:
                conn.connectTo("btspp://");
                break;
        }
        return conn;
    }

    public static void displayToastOnUIThread(String message) {
        Message message_holder = formMessage(message);
        message_holder.what = LeJOSDroid.TOAST;
        mUIMessageHandler.sendMessage(message_holder);
    }

    private static Message formMessage(String message) {
        Bundle b = new Bundle();
        b.putString(LeJOSDroid.MESSAGE_CONTENT, message);
        Message message_holder = new Message();
        message_holder.setData(b);
        return message_holder;
    }

    public static void sendMessageToUIThread(String message) {
        Message message_holder = formMessage(message);
        message_holder.what = LeJOSDroid.MESSAGE;
        mUIMessageHandler.sendMessage(message_holder);
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUIMessageHandler = new UIMessageHandler();
        setContentView(R.layout.main);
        _message = (TextView) findViewById(R.id.messageText);
        seupNXJCache();
        setupTachoCount(this);
        setupBTSend(this);
        setupRCNavigationControl(this);
        reusableToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (btSend != null) {
            Log.d(TAG, "onPause() closing btSend ");
            btSend.closeConnection();
            btSend = null;
        }
        if (tachoCount != null) {
            Log.d(TAG, "onPause() closing btSend ");
            tachoCount.closeConnection();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setupBTSend(final LeJOSDroid leJOSDroid) {
        Button button;
        button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                try {
                    btSend = new BTSend(mUIMessageHandler);
                    btSend.start();
                } catch (Exception e) {
                    Log.e(TAG, "failed to run BTSend:" + e.getMessage(), e);
                }
            }
        });
    }

    private void setupRCNavigationControl(final LeJOSDroid leJOSDroid) {
        Button button;
        button = (Button) findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent RCNavigationControl = new Intent(leJOSDroid, lejos.android.RCNavigationControl.class);
                startActivity(RCNavigationControl);
            }
        });
    }

    private void setupTachoCount(final LeJOSDroid mActivity) {
        Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                try {
                    tachoCount = new TachoCount();
                    _message.setVisibility(View.INVISIBLE);
                    tachoCount.start();
                } catch (Exception e) {
                    Log.e(TAG, "failed to run TachoCount:" + e.getMessage(), e);
                }
            }
        });
    }

    private void seupNXJCache() {
        File root = Environment.getExternalStorageDirectory();
        try {
            String androidCacheFile = "nxj.cache";
            File mLeJOS_dir = new File(root + "/leJOS");
            if (!mLeJOS_dir.exists()) {
                mLeJOS_dir.mkdir();
            }
            File mCacheFile = new File(root + "/leJOS/", androidCacheFile);
            if (root.canWrite() && !mCacheFile.exists()) {
                FileWriter gpxwriter = new FileWriter(mCacheFile);
                BufferedWriter out = new BufferedWriter(gpxwriter);
                out.write("");
                out.flush();
                out.close();
                _message.setText("nxj.cache (record of connection addresses) written to: " + mCacheFile.getName() + GO_AHEAD);
            } else {
                _message.setText("nxj.cache file not written as" + (!root.canWrite() ? mCacheFile.getName() + " can't be written to sdcard." : " cache already exists.") + GO_AHEAD);
            }
        } catch (IOException e) {
            Log.e(TAG, "Could not write nxj.cache " + e.getMessage(), e);
        }
        _message.setVisibility(View.VISIBLE);
        _message.requestLayout();
    }

    private void showToast(String textToShow) {
        reusableToast.setText(textToShow);
        reusableToast.show();
    }
}
