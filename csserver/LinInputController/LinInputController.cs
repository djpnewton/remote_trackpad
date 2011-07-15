using System;
namespace InputController
{
    public class LinInputController : IInputController
    {
        public void JoypadEvent(JoypadButton btn, bool depressed)
        {
            System.Console.WriteLine("JoypadEvent not yet implemented");
        }

        public void MouseEvent(int button, int dx, int dy)
        {
            System.Console.WriteLine("MouseEvent not yet implemented");
        }

        public void ScrollEvent(int dx, int dy)
        {
            System.Console.WriteLine("ScrollEvent not yet implemented");
        }

        public void KeyboardEvent(int code, bool shift)
        {
            System.Console.WriteLine("KeyboardEvent not yet implemented");
        }

        public void PlayKeyMacro(string keyMacro)
        {
            System.Console.WriteLine("PlayKeyMacro not yet implemented");
        }

        public string KeyCodeToMacro(int code, bool keyDown)
        {
            System.Console.WriteLine("KeyCodeToMacro not yet implemented");
            return "nyi! ";
        }

        public void VolumeUp()
        {
            System.Console.WriteLine("VolumeUp not yet implemented");
        }

        public void VolumeDown()
        {
            System.Console.WriteLine("VolumeDown not yet implemented");
        }
    }
}

