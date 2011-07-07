using System;
using System.Collections.Generic;
using System.Text;
using System.Runtime.InteropServices;

namespace InputServer
{
    public static class MouseEvent
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
    }
}
