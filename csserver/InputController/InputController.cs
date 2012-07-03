using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace InputController
{
    /**
     * The contants associated with the 'btn' parameter of the
     * '/joypad_event' OSC packet
     */
    public enum JoypadButton
    {
        DPAD_LEFT,
        DPAD_TOP,
        DPAD_RIGHT,
        DPAD_BOTTOM,
        BUTTON_START,
        BUTTON_SELECT,
        BUTTON_A,
        BUTTON_B,
    }

    /**
     * The contants associated with the 'code' parameter of the
     * '/keyboard_event' OSC packet
     */
    public enum Keyboard
    {
        KEYCODE_SHIFT = -1,
        KEYCODE_MODE_CHANGE = -2,
        KEYCODE_CANCEL = -3,
        KEYCODE_DONE = -4,
        KEYCODE_DELETE = -5,
        KEYCODE_ALT = -6,
        KEYCODE_ENTER = -7
    }

    public interface IInputController
    {
        /**
         * Perform a joypad event
         * @param btn The joypad button that has changed state
         * @param depressed The btn is down (true) or up (false)
         */
        void JoypadEvent(JoypadButton btn, bool depressed);

        /**
         * Perform a mouse event
         * @param button The button state (0 = none, 1 = left, 2 = right)
         * @param dx The change in the X axis
         * @param dy The change in the Y axis
         */
        void MouseEvent(int button, int dx, int dy);

        /**
         * Perform a scroll event
         * @param dx The change in the X axis
         * @param dy The change in the Y axis
         */
        void ScrollEvent(int dx, int dy);

        /**
         * Perform a keyboard event
         * @param code The key code
         * @param shift Is the shift key depressed
         */
        void KeyboardEvent(int code, bool shift);

        /**
         * Play back a keyboard macro
         * @param keyMacro The string of encoded key codes
         */
        void PlayKeyMacro(string keyMacro);

        /**
         * Convert a system key code to a encoded keyboard
         * macro string
         * @param code The key code
         * @param keyDown True if this is a key down event
         * (false if it is a key up event)
         */
        string KeyCodeToMacro(int code, bool keyDown);

        /**
         * Turn up the system volume
         */
        void VolumeUp();

        /**
         * Turn down the system volume
         */
        void VolumeDown();
    }
}
