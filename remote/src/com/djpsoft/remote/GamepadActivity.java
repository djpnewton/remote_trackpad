package com.djpsoft.remote;

import android.app.Activity;
import android.os.Bundle;

public class GamepadActivity extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GamepadView view = new GamepadView(this);
        setContentView(view);
    }



}
