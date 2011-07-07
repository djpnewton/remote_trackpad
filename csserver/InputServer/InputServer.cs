using System;
using System.Collections.Generic;
using System.Text;
using System.Net;
using Bespoke.Common.Osc;
using WindowsInput;

namespace InputServer
{
    public class InputServer : IDisposable
    {
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

        public enum Keyboard
        {
            KEYCODE_SHIFT = -1,
            KEYCODE_MODE_CHANGE = -2,
            KEYCODE_CANCEL = -3,
            KEYCODE_DONE = -4,
            KEYCODE_DELETE = -5,
            KEYCODE_ALT = -6
        }

        public const int DEFAULT_VIP_PORT = 12321;

        public const String JOYPAD_BUTTON_EVENT = "/joypad_button_event";
        public const String MOUSE_EVENT = "/mouse_event";
        public const String SCROLL_EVENT = "/scroll_event";
        public const String KEYBOARD_EVENT = "/keyboard_event";
        public const String VOLUME_EVENT = "/volume_event";

        int lastButtonState = 0;

        ILog log;
        OscServer server;

        public bool UseVolumeMacros { get; set; }

        public string VolumeUpMacro { get; set; }

        public string VolumeDownMacro { get; set; }

        public InputServer(ILog log)
        {
            this.log = log;
            server = new OscServer(TransportType.Udp, IPAddress.Any, DEFAULT_VIP_PORT);
            server.RegisterMethod(JOYPAD_BUTTON_EVENT);
            server.RegisterMethod(MOUSE_EVENT);
            server.RegisterMethod(SCROLL_EVENT);
            server.RegisterMethod(KEYBOARD_EVENT);
            server.RegisterMethod(VOLUME_EVENT);
            server.MessageReceived += new OscMessageReceivedHandler(server_MessageReceived);
            server.Start();
            log.Log(LogLevel.Info, "Starting Input Server");
        }

        public void Dispose()
        {
            log.Log(LogLevel.Info, "Stopping Input Server");
            if (server.IsRunning)
                server.Stop();
        }

        int reverseEndianizeOscInt(Object o)
        {
            return IPAddress.NetworkToHostOrder((int)o);
        }

        void server_MessageReceived(object sender, OscMessageReceivedEventArgs e)
        {
            log.Log(LogLevel.Info, "Message Recieved");
            log.Log(LogLevel.Info, "  " + e.Message.Address);
            if (e.Message.Address == JOYPAD_BUTTON_EVENT)
            {
                JoypadButton btn = (JoypadButton)reverseEndianizeOscInt(e.Message.Data[0]);
                bool depressed = reverseEndianizeOscInt(e.Message.Data[1]) != 0;
                log.Log(LogLevel.Info, string.Format("    {0} - {1}", btn, depressed));

                VirtualKeyCode code = VirtualKeyCode.VK_A;
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
                }
                if (depressed)
                    InputSimulator.SimulateKeyDown(code);
                else
                    InputSimulator.SimulateKeyUp(code);
            }
            else if (e.Message.Address == MOUSE_EVENT)
            {
                int btn = reverseEndianizeOscInt(e.Message.Data[0]);
                int dx = reverseEndianizeOscInt(e.Message.Data[1]);
                int dy = reverseEndianizeOscInt(e.Message.Data[2]);
                log.Log(LogLevel.Info, string.Format("    btn: {0}, dx: {1}, dy: {2}", btn, dx, dy));
                uint mouseEventFlags = (uint)MouseEvent.MouseEventFlags.MOVE;
                if (lastButtonState != 1 && btn == 1)
                    mouseEventFlags |= (uint)MouseEvent.MouseEventFlags.LEFTDOWN;
                if (lastButtonState == 1 && btn != 1)
                    mouseEventFlags |= (uint)MouseEvent.MouseEventFlags.LEFTUP;
                if (lastButtonState != 2 && btn == 2)
                    mouseEventFlags |= (uint)MouseEvent.MouseEventFlags.RIGHTDOWN;
                if (lastButtonState == 2 && btn != 2)
                    mouseEventFlags |= (uint)MouseEvent.MouseEventFlags.RIGHTUP;
                MouseEvent.mouse_event(mouseEventFlags, (uint)dx, (uint)dy, 0, UIntPtr.Zero);
                lastButtonState = btn;
            }
            else if (e.Message.Address == SCROLL_EVENT)
            {
                int dx = reverseEndianizeOscInt(e.Message.Data[0]);
                int dy = reverseEndianizeOscInt(e.Message.Data[1]);
                log.Log(LogLevel.Info, string.Format("    dx: {0}, dy: {1}", dx, dy));
                MouseEvent.mouse_event((uint)MouseEvent.MouseEventFlags.WHEEL, 0, 0, (uint)(-dy * 20), UIntPtr.Zero);
                MouseEvent.mouse_event((uint)MouseEvent.MouseEventFlags.HWHEEL, 0, 0, (uint)(dx * 20), UIntPtr.Zero);
            }
            else if (e.Message.Address == KEYBOARD_EVENT)
            {
                int code = reverseEndianizeOscInt(e.Message.Data[0]);
                bool shift = reverseEndianizeOscInt(e.Message.Data[1]) != 0;
                log.Log(LogLevel.Info, string.Format("    code: {0,2:X}, '{1}', shift: {2}", code, (char)code, shift));
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
            else if (e.Message.Address == VOLUME_EVENT)
            {
                int d = reverseEndianizeOscInt(e.Message.Data[0]);
                log.Log(LogLevel.Info, string.Format("    d: {0}", d));
                if (UseVolumeMacros)
                {
                    List<KeyHelper.KeyState> keys;
                    if (d == 1)
                        keys = KeyHelper.StringToKeyStates(VolumeUpMacro);
                    else
                        keys = KeyHelper.StringToKeyStates(VolumeDownMacro);
                    foreach (KeyHelper.KeyState ks in keys)
                    {
                        if (ks.KeyDown)
                            InputSimulator.SimulateKeyDown((VirtualKeyCode)ks.Key);
                        else
                            InputSimulator.SimulateKeyUp((VirtualKeyCode)ks.Key);
                    }
                }
                else
                {
                    if (d == 1)
                        SoundVolume.VolumeUp();
                    else
                        SoundVolume.VolumeDown();
                }
            }
        }
    }
}
