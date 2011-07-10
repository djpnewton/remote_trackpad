package com.djpsoft.remote;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import net.robotmedia.billing.AbstractBillingActivity;

import com.djpsoft.remote.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

public class RemoteSetup extends AbstractBillingActivity {

    private static final String TAG = "RemoteSetup";

    public static final String PREFS_NAME = "VIRTUAL_INPUT_PREFS";
    public static final String HANDSHAKE_FIRST = "HANDSHAKE_FIRST";
    public static final String HOSTNAME = "HOSTNAME";
    public static final String PRO_FUNCTIONS = "android.test.purchased";

    public static OSCClient oscClient = null;

    private CheckBox cbTapToClick;
    private CheckBox cbDoubleTapAndDrag;
    private CheckBox cbTwoFingerTapRightClick;
    private CheckBox cbTwoFingerDragScroll;
    private CheckBox cbShowButtons;
    private CheckBox cbLandscape;
    private CheckBox cbHandshakeFirst;
    private EditText edHostname;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup);

        cbTapToClick = (CheckBox) findViewById(R.id.tap_to_click);
        cbDoubleTapAndDrag = (CheckBox) findViewById(R.id.double_tap_and_drag);
        cbTwoFingerTapRightClick = (CheckBox) findViewById(R.id.two_finger_tap_right_click);
        cbTwoFingerDragScroll = (CheckBox) findViewById(R.id.two_finger_drag_scroll);
        cbShowButtons = (CheckBox) findViewById(R.id.show_buttons);
        cbLandscape = (CheckBox) findViewById(R.id.landscape);
        cbHandshakeFirst = (CheckBox) findViewById(R.id.handshake_first);
        edHostname = (EditText) findViewById(R.id.hostname);

        loadPrefs();

        cbTapToClick.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                savePrefs();
            }
        });
        cbDoubleTapAndDrag.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                savePrefs();
            }
        });
        cbTwoFingerTapRightClick.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                savePrefs();
            }
        });
        cbTwoFingerDragScroll.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                savePrefs();
            }
        });
        cbShowButtons.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                savePrefs();
            }
        });
        cbHandshakeFirst.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                savePrefs();
            }
        });
        cbLandscape.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                savePrefs();
            }
        });

        Button btn = (Button) findViewById(R.id.go_gamepad);
        final Intent gamepadIntent = new Intent(this, GamepadActivity.class);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                savePrefs();
                if (checkHandshake()) {
                    createOscClient();
                    startActivity(gamepadIntent);
                }
            }
        });
        btn = (Button) findViewById(R.id.go_trackpad);
        final Intent trackpadIntent = new Intent(this, TrackpadActivity.class);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                savePrefs();
                if (checkHandshake()) {
                    createOscClient();
                    trackpadIntent.putExtra(TrackpadView.TAP_TO_CLICK, cbTapToClick.isChecked());
                    trackpadIntent.putExtra(TrackpadView.DOUBLE_TAP_AND_DRAG, cbDoubleTapAndDrag.isChecked());
                    trackpadIntent.putExtra(TrackpadView.TWO_FINGER_TAP_RIGHT_CLICK, cbTwoFingerTapRightClick.isChecked());
                    trackpadIntent.putExtra(TrackpadView.TWO_FINGER_DRAG_SCROLL, cbTwoFingerDragScroll.isChecked());
                    trackpadIntent.putExtra(TrackpadView.SHOW_BUTTONS, cbShowButtons.isChecked());
                    trackpadIntent.putExtra(TrackpadActivity.LANDSCAPE, cbLandscape.isChecked());
                    startActivity(trackpadIntent);
                }
            }
        });
        btn = (Button) findViewById(R.id.go_testtouch);
        final Intent touchTestIntent = new Intent(this, TouchTestActivity.class);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                savePrefs();
                if (checkHandshake()) {
                    createOscClient();
                    startActivity(touchTestIntent);
                }
            }
        });
        btn = (Button) findViewById(R.id.send_ping);
        final Context con = this;
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // send discovery message
                final OSCDiscover discover = new OSCDiscover(con,
                    OSCDiscover.DEFAULT_MULTICAST_ADDR,
                    OSCDiscover.DEFAULT_MULTICAST_PORT);
                if (discover.isWifiEnabled()) {
                    String host = discover.multicastPing();
                    /*
                    // debug scenario
                    if (host == "") {
                        AlertDialog.Builder builder = new AlertDialog.Builder(con);
                        builder.setMessage("Server not found via multicast. Would you like to try a brute force search on " + discover.getIpAddressString() + "'s subnet?")
                               .setCancelable(false)
                               .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        String host = discover.bruteforcePing();
                                        if (host != "")
                                            edHostname.setText(host);
                                    }
                                })
                               .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                    else {
                        edHostname.setText(host);
                    }
                    */
                    if (host != "") {
                        Toast toast = Toast.makeText(con, R.string.found_server, Toast.LENGTH_SHORT);
                        toast.show();
                        edHostname.setText(host);
                    }
                    else {
                        Log.w(TAG, "discover.multicastPing() failed");
                        host = discover.bruteforcePing();
                        if (host != "") {
                            Toast toast = Toast.makeText(con, R.string.found_server, Toast.LENGTH_SHORT);
                            toast.show();
                            edHostname.setText(host);
                        }
                        else {
                            Log.w(TAG, "bruteforcePing() failed");
                            Toast toast = Toast.makeText(con, R.string.failed_to_discover_server, Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                }
                else {
                    final Button btnEnableWifi = (Button) findViewById(R.id.enable_wifi);
                    btnEnableWifi.setVisibility(View.VISIBLE);
                    btnEnableWifi.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (discover.enableWifi())
                                btnEnableWifi.setVisibility(View.GONE);
                            else {
                                Toast toast = Toast.makeText(con, R.string.failed_to_enable_wifi, Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                    });
                    Toast toast = Toast.makeText(con, R.string.wifi_not_enabled, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        btn = (Button) findViewById(R.id.go_pro);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPurchase(PRO_FUNCTIONS);
            }
        });
    }

    private void loadPrefs() {
       SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
       cbTapToClick.setChecked(settings.getBoolean(TrackpadView.TAP_TO_CLICK, true));
       cbDoubleTapAndDrag.setChecked(settings.getBoolean(TrackpadView.DOUBLE_TAP_AND_DRAG, true));
       cbTwoFingerTapRightClick.setChecked(settings.getBoolean(TrackpadView.TWO_FINGER_TAP_RIGHT_CLICK, true));
       cbTwoFingerDragScroll.setChecked(settings.getBoolean(TrackpadView.TWO_FINGER_DRAG_SCROLL, true));
       cbShowButtons.setChecked(settings.getBoolean(TrackpadView.SHOW_BUTTONS, false));
       cbLandscape.setChecked(settings.getBoolean(TrackpadActivity.LANDSCAPE, true));
       cbHandshakeFirst.setChecked(settings.getBoolean(HANDSHAKE_FIRST, true));
       edHostname.setText(settings.getString(HOSTNAME, ""));
    }

    private void savePrefs() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(TrackpadView.TAP_TO_CLICK, cbTapToClick.isChecked());
        editor.putBoolean(TrackpadView.DOUBLE_TAP_AND_DRAG, cbDoubleTapAndDrag.isChecked());
        editor.putBoolean(TrackpadView.TWO_FINGER_TAP_RIGHT_CLICK, cbTwoFingerTapRightClick.isChecked());
        editor.putBoolean(TrackpadView.TWO_FINGER_DRAG_SCROLL, cbTwoFingerDragScroll.isChecked());
        editor.putBoolean(TrackpadView.SHOW_BUTTONS, cbShowButtons.isChecked());
        editor.putBoolean(TrackpadActivity.LANDSCAPE, cbLandscape.isChecked());
        editor.putBoolean(HANDSHAKE_FIRST, cbHandshakeFirst.isChecked());
        editor.putString(HOSTNAME, edHostname.getText().toString());
        editor.commit();
    }

    private boolean checkHandshake() {
        if (!cbHandshakeFirst.isChecked())
            return true;
        else
            if (OSCDiscover.checkHandshake(edHostname.getText().toString(),
                    OSCDiscover.DEFAULT_MULTICAST_PORT)) {
                return true;
            }
            else {
                Toast toast = Toast.makeText(this, R.string.handshake_failed, Toast.LENGTH_SHORT);
                toast.show();
                Log.e(TAG, "checkHandshake failed");
                return false;
            }
    }

    private void createOscClient() {
        if (oscClient != null)
            oscClient.close();
        // create osc client
        try {
            EditText edHost = (EditText) findViewById(R.id.hostname);
            InetAddress addr = InetAddress.getByName(edHost.getText().toString());
            oscClient = new OSCClient(addr, OSCClient.DEFAULT_VIP_PORT);
        }
        catch (SocketException e) {
            oscClient = null;
            Log.e(TAG, "createOscClient: SocketException");
        }
        catch (UnknownHostException e) {
            oscClient = null;
            Log.e(TAG, "createOscClient: UnknownHostException");
        }
    }

    @Override
    public void onBillingChecked(boolean supported) {
        if (supported) {
            Button btn = (Button) findViewById(R.id.go_pro);
            btn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPurchaseCancelled(String itemId) {
    }

    @Override
    public void onPurchaseExecuted(String itemId) {
        if (itemId == PRO_FUNCTIONS) {
            // Show pro functions
            Button btn = (Button) findViewById(R.id.send_ping);
            btn.setVisibility(View.VISIBLE);
            // Hide upgrade button
            btn = (Button) findViewById(R.id.go_pro);
            btn.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPurchaseRefunded(String itemId) {
        if (itemId == PRO_FUNCTIONS) {
            // Hide pro functions
            Button btn = (Button) findViewById(R.id.send_ping);
            btn.setVisibility(View.GONE);
            // Show upgrade button
            btn = (Button) findViewById(R.id.go_pro);
            btn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public byte[] getObfuscationSalt() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getPublicKey() {
        // TODO Auto-generated method stub
        return null;
    }
}
