package com.djpsoft.remote;

import java.io.IOException;

import com.djpsoft.remote.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.widget.Toast;

public class GamepadView extends TouchView {

    Dpad dpad;
    JoyButton buttonA, buttonB;

    public enum JoypadButton {
        DPAD_LEFT,
        DPAD_TOP,
        DPAD_RIGHT,
        DPAD_BOTTOM,
        BUTTON_START,
        BUTTON_SELECT,
        BUTTON_A,
        BUTTON_B,
    }

    boolean dpadLeft = false;
    boolean dpadTop = false;
    boolean dpadRight = false;
    boolean dpadBottom = false;
    boolean buttonStart = false;
    boolean buttonSelect = false;
    boolean buttonA_ = false;
    boolean buttonB_ = false;


    public GamepadView(Context context) {
        super(context);
        InitLayout(context);
    }

    private void InitLayout(Context context) {
        float offsetx, offsety;

        // dpad object
        dpad = new Dpad(context, 0, 0);
        int dpadWidth = dpad.getWidth();
        int dpadHeight = dpad.getHeight();
        offsetx = getWidth() / 4.0f - dpadWidth / 2.0f;
        offsety = getHeight() / 2.0f - dpadHeight / 2.0f;
        dpad.setX(offsetx);
        dpad.setY(offsety);
        dpad.setBorderX((getWidth() / 2.0f - dpadWidth) / 2.0f);
        dpad.setBorderY((getHeight() - dpadHeight) / 2.0f);

        // button objects
        Drawable button = context.getResources().getDrawable(R.drawable.button);
        button.setBounds(0, 0, button.getIntrinsicWidth(), button.getIntrinsicHeight());
        Drawable buttonDepressed = context.getResources().getDrawable(R.drawable.button_depressed);
        buttonDepressed.setBounds(0, 0, buttonDepressed.getIntrinsicWidth(), buttonDepressed.getIntrinsicHeight());
        buttonA = new JoyButton(button, buttonDepressed, 0, 0);
        buttonB = new JoyButton(button, buttonDepressed, 0, 0);
        offsetx = (getWidth() * 5 / 8.0f) - (button.getBounds().width() / 2.0f);
        offsety = getHeight() / 2.0f - button.getBounds().height() / 2.0f;
        buttonA.setX(offsetx);
        buttonA.setY(offsety);
        offsetx = (getWidth() * 7 / 8.0f) - (button.getBounds().width() / 2.0f);
        buttonB.setX(offsetx);
        buttonB.setY(offsety);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        InitLayout(this.context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        // check dpad state
        SetStateAndSend(JoypadButton.DPAD_LEFT, dpad.AnyPointInDpadLeft(pts));
        SetStateAndSend(JoypadButton.DPAD_TOP, dpad.AnyPointInDpadTop(pts));
        SetStateAndSend(JoypadButton.DPAD_RIGHT, dpad.AnyPointInDpadRight(pts));
        SetStateAndSend(JoypadButton.DPAD_BOTTOM, dpad.AnyPointInDpadBottom(pts));

        // check button states
        SetStateAndSend(JoypadButton.BUTTON_A, buttonA.AnyPointInBounds(pts));
        SetStateAndSend(JoypadButton.BUTTON_B, buttonB.AnyPointInBounds(pts));

        // redraw screen
        invalidate();

        // we have handled the touch event
        return true;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // draw dpad
        dpad.Draw(canvas);
        if (GetJoypadButtonState(JoypadButton.DPAD_LEFT))
           dpad.Draw(canvas, JoypadButton.DPAD_LEFT);
        if (GetJoypadButtonState(JoypadButton.DPAD_TOP))
            dpad.Draw(canvas, JoypadButton.DPAD_TOP);
        if (GetJoypadButtonState(JoypadButton.DPAD_RIGHT))
            dpad.Draw(canvas, JoypadButton.DPAD_RIGHT);
        if (GetJoypadButtonState(JoypadButton.DPAD_BOTTOM))
            dpad.Draw(canvas, JoypadButton.DPAD_BOTTOM);

        // draw buttons
        buttonA.Draw(canvas, GetJoypadButtonState(JoypadButton.BUTTON_A));
        buttonB.Draw(canvas, GetJoypadButtonState(JoypadButton.BUTTON_B));

        // draw touch locations
        drawTouchPoints(canvas);
    }

    private void SetStateAndSend(JoypadButton button, boolean depressed) {
        if (RemoteSetup.oscClient != null) {
            try {
                if (SetJoypadButtonState(button, depressed))
                    RemoteSetup.oscClient.SendJoypadButtonEvent(button, depressed);
            }
            catch (IOException e) {
                // TODO: make this better
                Toast toast = Toast.makeText(context, "Shiiiiieet IO execption!", Toast.LENGTH_LONG);
                toast.show();
            }
        }
        else {
            // TODO: warn user?
        }
    }

    public boolean SetJoypadButtonState(JoypadButton button, boolean depressed) {
        switch (button) {
        case DPAD_LEFT:
            if (dpadLeft != depressed) {
                dpadLeft = depressed;
                return true;
            }
            break;

        case DPAD_TOP:
            if (dpadTop != depressed) {
                dpadTop = depressed;
                return true;
            }
            break;

        case DPAD_RIGHT:
            if (dpadRight != depressed) {
                dpadRight = depressed;
                return true;
            }
            break;

        case DPAD_BOTTOM:
            if (dpadBottom != depressed) {
                dpadBottom = depressed;
                return true;
            }
            break;

        case BUTTON_SELECT:
            if (buttonSelect != depressed) {
                buttonSelect = depressed;
                return true;
            }
            break;

        case BUTTON_START:
            if (buttonStart != depressed) {
                buttonStart = depressed;
                return true;
            }
            break;

        case BUTTON_A:
            if (buttonA_ != depressed) {
                buttonA_ = depressed;
                return true;
            }
            break;

        case BUTTON_B:
            if (buttonB_ != depressed) {
                buttonB_ = depressed;
                return true;
            }
            break;
        }
        return false;
    }

    public boolean GetJoypadButtonState(JoypadButton button) {
        switch (button) {
        case DPAD_LEFT:
            return dpadLeft;

        case DPAD_TOP:
            return dpadTop;

        case DPAD_RIGHT:
            return dpadRight;

        case DPAD_BOTTOM:
            return dpadBottom;

        case BUTTON_SELECT:
            return buttonSelect;

        case BUTTON_START:
            return buttonStart;

        case BUTTON_A:
            return buttonA_;

        case BUTTON_B:
            return buttonB_;
        }
        return false;
    }
}
