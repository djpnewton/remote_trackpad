package com.djpsoft.remote;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class TrackpadActivity extends Activity {

    private static final String TAG = "TrackpadActivity";

    public static final String KEYBOARD_VISIBLE = "KEYBOARD_VISIBLE";
    public static final String LANDSCAPE = "LANDSCAPE";

    private boolean keyboardVisible = false;
    private SoftKeyboard kbd;

    private boolean tapToClick = false;
    private boolean doubleTapAndDrag = false;
    private boolean twoFingerTapRightClick = false;
    private boolean twoFingerDragScroll = false;
    private boolean showButtons = true;
    private boolean landscape = true;

    private boolean preventExcessIOMsgs = false;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            keyboardVisible = savedInstanceState.getBoolean(KEYBOARD_VISIBLE);
            tapToClick = savedInstanceState.getBoolean(TrackpadView.TAP_TO_CLICK);
            doubleTapAndDrag = savedInstanceState.getBoolean(TrackpadView.DOUBLE_TAP_AND_DRAG);
            twoFingerTapRightClick = savedInstanceState.getBoolean(TrackpadView.TWO_FINGER_TAP_RIGHT_CLICK);
            twoFingerDragScroll = savedInstanceState.getBoolean(TrackpadView.TWO_FINGER_DRAG_SCROLL);
            showButtons = savedInstanceState.getBoolean(TrackpadView.SHOW_BUTTONS);
            landscape = savedInstanceState.getBoolean(LANDSCAPE);
        }
        else {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                tapToClick = extras.getBoolean(TrackpadView.TAP_TO_CLICK);
                doubleTapAndDrag = extras.getBoolean(TrackpadView.DOUBLE_TAP_AND_DRAG);
                twoFingerTapRightClick = extras.getBoolean(TrackpadView.TWO_FINGER_TAP_RIGHT_CLICK);
                twoFingerDragScroll = extras.getBoolean(TrackpadView.TWO_FINGER_DRAG_SCROLL);
                showButtons = extras.getBoolean(TrackpadView.SHOW_BUTTONS);
                landscape = extras.getBoolean(LANDSCAPE);
            }
        }

        if (landscape)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        else
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setupMouseView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEYBOARD_VISIBLE, keyboardVisible);
        outState.putBoolean(TrackpadView.TAP_TO_CLICK, tapToClick);
        outState.putBoolean(TrackpadView.DOUBLE_TAP_AND_DRAG, doubleTapAndDrag);
        outState.putBoolean(TrackpadView.TWO_FINGER_TAP_RIGHT_CLICK, twoFingerTapRightClick);
        outState.putBoolean(TrackpadView.TWO_FINGER_DRAG_SCROLL, twoFingerDragScroll);
        outState.putBoolean(TrackpadView.SHOW_BUTTONS, showButtons);
        outState.putBoolean(LANDSCAPE, landscape);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 82 /* Menu Button */)
        {
            toggleView();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
            switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_UP) {
                    performEvent(1);
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_UP) {
                    performEvent(-1);
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
            }
        }

    void performEvent(int d) {
        if (RemoteSetup.oscClient != null) {
            try {
                RemoteSetup.oscClient.SendVolumeEvent(d);
            }
            catch (IOException e) {
                Log.e(TAG, "performEvent: IOException");
                if (preventExcessIOMsgs == false) {
                    preventExcessIOMsgs = true;
                    Toast toast = Toast.makeText(this, R.string.send_msg_io_exception, Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        }
        else {
            Log.e(TAG, "performEvent: RemoteSetup.oscClient == null");
        }
    }

    private void toggleView() {
        keyboardVisible = !keyboardVisible;
        if (keyboardVisible)
            setupKeyboardView();
        else
            setupMouseView();
    }

    private View createViewContainer(View mainView) {
        LinearLayout ll = new LinearLayout(this);
        AdView adView = new AdView(this, AdSize.BANNER, getString(R.string.ad_id));
        adView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        ll.addView(adView);
        ll.setOrientation(LinearLayout.VERTICAL);
        mainView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));
        ll.addView(mainView);
        setContentView(ll);
        adView.loadAd(new AdRequest());
        return ll;
    }

    private void setupMouseView() {
        TrackpadView view = new TrackpadView(this, tapToClick, doubleTapAndDrag, twoFingerTapRightClick, twoFingerDragScroll, showButtons);
        createViewContainer(view);
    }

    private void setupKeyboardView() {
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.kbd, null);
        KeyboardView kbdView = (KeyboardView) view.findViewById(R.id.kbd);
        kbd = new SoftKeyboard(this, kbdView, new SoftKeyboard.CloseKeyboardListener() {
            @Override
            public void function() {
                toggleView();
            }
        });
        createViewContainer(view);
    }
}
