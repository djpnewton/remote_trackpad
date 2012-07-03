package com.djpsoft.remote;

import java.io.IOException;

import com.djpsoft.remote.R;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

//
// NOTE: custom soft keyboard resources nicked from
//       http://developer.android.com/resources/samples/SoftKeyboard/index.html
//

public class SoftKeyboard
        implements KeyboardView.OnKeyboardActionListener {

    private static final String TAG = "SoftKeyboard";

    public interface CloseKeyboardListener {
        void function();
    }

    private Context context;

    private KeyboardView kbdView;
    private Keyboard qwertyKeyboard;
    private Keyboard symbolsKeyboard;
    private Keyboard symbolsShiftedKeyboard;

    private CloseKeyboardListener closeKbd;

    private boolean mCapsLock = false;

    private boolean preventExcessIOMsgs = false;

    public SoftKeyboard(Context context, KeyboardView kbdView, CloseKeyboardListener closeKbd) {
        this.context = context;
        this.kbdView = kbdView;
        this.closeKbd = closeKbd;

        qwertyKeyboard = new Keyboard(context, R.xml.qwerty);
        symbolsKeyboard = new Keyboard(context, R.xml.symbols);
        symbolsShiftedKeyboard = new Keyboard(context, R.xml.symbols_shift);

        kbdView.setOnKeyboardActionListener(this);
        kbdView.setKeyboard(qwertyKeyboard);
        kbdView.setEnabled(true);
        kbdView.setPreviewEnabled(true);
        kbdView.setVisibility(View.VISIBLE);
    }

    public void onPress(int primaryCode) {
    }
    public void onRelease(int primaryCode) {
    }

    public void onKey(int primaryCode, int[] keyCodes) {
        if (primaryCode == Keyboard.KEYCODE_DELETE) {
            sendCode(primaryCode);
        }
        else if (primaryCode == Keyboard.KEYCODE_SHIFT) {
            handleShift();
        }
        else if (primaryCode == Keyboard.KEYCODE_CANCEL) {
            if (closeKbd != null)
                closeKbd.function();
        }
        else if (primaryCode == Keyboard.KEYCODE_MODE_CHANGE) {
            Keyboard current = kbdView.getKeyboard();
            if (current == symbolsKeyboard || current == symbolsShiftedKeyboard) {
                mCapsLock = false;
                current = qwertyKeyboard;
            } else {
                mCapsLock = false;
                current = symbolsKeyboard;
            }
            kbdView.setKeyboard(current);
            if (current == symbolsKeyboard) {
                current.setShifted(false);
            }
        }
        else {
            handleCharacter(primaryCode, keyCodes);
        }

    }

    private void handleShift() {
        if (kbdView == null) {
            return;
        }

        Keyboard currentKeyboard = kbdView.getKeyboard();
        if (qwertyKeyboard == currentKeyboard) {
            // Alphabet keyboard
            mCapsLock = !mCapsLock;
            kbdView.setShifted(mCapsLock);
        }
        else if (currentKeyboard == symbolsKeyboard) {
            mCapsLock = false;
            symbolsKeyboard.setShifted(true);
            kbdView.setKeyboard(symbolsShiftedKeyboard);
            symbolsShiftedKeyboard.setShifted(true);
        }
        else if (currentKeyboard == symbolsShiftedKeyboard) {
            mCapsLock = false;
            symbolsShiftedKeyboard.setShifted(false);
            kbdView.setKeyboard(symbolsKeyboard);
            symbolsKeyboard.setShifted(false);
        }
    }

    private void handleCharacter(int primaryCode, int[] keyCodes) {
        sendCode(primaryCode);
    }

    private void sendCode(int code) {
        if (RemoteSetup.oscClient != null) {
            try {
                boolean shift = mCapsLock && kbdView.getKeyboard() == qwertyKeyboard;
                RemoteSetup.oscClient.SendKeyboardEvent(code, shift);
            }
            catch (IOException e) {
                Log.e(TAG, "sendCode: IOException");
                if (preventExcessIOMsgs == false) {
                    preventExcessIOMsgs = true;
                    Toast toast = Toast.makeText(context, R.string.send_msg_io_exception, Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        }
        else {
            Log.e(TAG, "sendCode: RemoteSetup.oscClient == null");
        }
    }

    public void onText(CharSequence text) {
    }

    public void swipeRight() {
    }

    public void swipeLeft() {
    }

    public void swipeUp() {
    }

    public void swipeDown() {
    }
}
