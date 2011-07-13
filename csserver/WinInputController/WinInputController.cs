using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Runtime.InteropServices;
using InputController;
using WindowsInput;

namespace InputController
{
    public class WinInputController : IInputController
    {
        [Flags]
        public enum MouseEventFlags : uint
        {
            LEFTDOWN = 0x00000002,
            LEFTUP = 0x00000004,
            MIDDLEDOWN = 0x00000020,
            MIDDLEUP = 0x00000040,
            MOVE = 0x00000001,
            ABSOLUTE = 0x00008000,
            RIGHTDOWN = 0x00000008,
            RIGHTUP = 0x00000010,
            WHEEL = 0x00000800,
            HWHEEL = 0x00001000,
            XDOWN = 0x00000080,
            XUP = 0x00000100
        }

        [DllImport("user32.dll")]
        public static extern void mouse_event(uint dwFlags, uint dx, uint dy, uint dwData,
           UIntPtr dwExtraInfo);

        int lastButtonState = 0;

        public void JoypadEvent(JoypadButton btn, bool depressed)
        {
            VirtualKeyCode code;
            switch (btn)
            {
                case JoypadButton.DPAD_LEFT:
                    code = VirtualKeyCode.LEFT;
                    break;
                case JoypadButton.DPAD_TOP:
                    code = VirtualKeyCode.UP;
                    break;
                case JoypadButton.DPAD_RIGHT:
                    code = VirtualKeyCode.RIGHT;
                    break;
                case JoypadButton.DPAD_BOTTOM:
                    code = VirtualKeyCode.DOWN;
                    break;
                case JoypadButton.BUTTON_START:
                    code = VirtualKeyCode.RETURN;
                    break;
                case JoypadButton.BUTTON_SELECT:
                    code = VirtualKeyCode.SPACE;
                    break;
                case JoypadButton.BUTTON_A:
                    code = VirtualKeyCode.VK_Z;
                    break;
                case JoypadButton.BUTTON_B:
                    code = VirtualKeyCode.VK_X;
                    break;
                default:
                    code = VirtualKeyCode.VK_A;
                    break;
            }
            if (depressed)
                InputSimulator.SimulateKeyDown(code);
            else
                InputSimulator.SimulateKeyUp(code);
        }

        public void MouseEvent(int button, int dx, int dy)
        {
            uint mouseEventFlags = (uint)MouseEventFlags.MOVE;
            if (lastButtonState != 1 && button == 1)
                mouseEventFlags |= (uint)MouseEventFlags.LEFTDOWN;
            if (lastButtonState == 1 && button != 1)
                mouseEventFlags |= (uint)MouseEventFlags.LEFTUP;
            if (lastButtonState != 2 && button == 2)
                mouseEventFlags |= (uint)MouseEventFlags.RIGHTDOWN;
            if (lastButtonState == 2 && button != 2)
                mouseEventFlags |= (uint)MouseEventFlags.RIGHTUP;
            mouse_event(mouseEventFlags, (uint)dx, (uint)dy, 0, UIntPtr.Zero);
            lastButtonState = button;
        }

        public void ScrollEvent(int dx, int dy)
        {
            mouse_event((uint)MouseEventFlags.WHEEL, 0, 0, (uint)(-dy * 20), UIntPtr.Zero);
            mouse_event((uint)MouseEventFlags.HWHEEL, 0, 0, (uint)(dx * 20), UIntPtr.Zero);
        }

        public void KeyboardEvent(int code, bool shift)
        {
            switch ((Keyboard)code)
            {
                case Keyboard.KEYCODE_DELETE:
                    InputSimulator.SimulateKeyPress(VirtualKeyCode.BACK);
                   break;
                case Keyboard.KEYCODE_ALT:
                case Keyboard.KEYCODE_CANCEL:
                case Keyboard.KEYCODE_DONE:
                case Keyboard.KEYCODE_MODE_CHANGE:
                case Keyboard.KEYCODE_SHIFT:
                    break;
                default:
                    string s = ((char)code).ToString();
                    if (shift)
                        s = s.ToUpper();
                    InputSimulator.SimulateTextEntry(s);
                    break;
            }
        }

        public void SysKeyDown(int code)
        {
            InputSimulator.SimulateKeyDown((VirtualKeyCode)code);
        }

        public void SysKeyUp(int code)
        {
            InputSimulator.SimulateKeyUp((VirtualKeyCode)code);
        }

        public void VolumeUp()
        {
            SoundVolume.VolumeUp();
        }

        public void VolumeDown()
        {
            SoundVolume.VolumeDown();
        }
    }
}
