package com.djpsoft.remote;

import android.app.Activity;
import android.os.Bundle;

public class TouchTestActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TouchView view = new TouchView(this);
        view.InvalidateOnTouch = true;
        view.DrawTouchPoints = true;
        setContentView(view);
    }
}
