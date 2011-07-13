using System;
using System.Collections.Generic;
using System.Text;
using System.Net;
using Bespoke.Common.Osc;
using InputController;

namespace InputServer
{
    public class InputServer : IDisposable
    {
        public const int DEFAULT_VIP_PORT = 12321;

        public const String JOYPAD_BUTTON_EVENT = "/joypad_button_event";
        public const String MOUSE_EVENT = "/mouse_event";
        public const String SCROLL_EVENT = "/scroll_event";
        public const String KEYBOARD_EVENT = "/keyboard_event";
        public const String VOLUME_EVENT = "/volume_event";

        ILog log;
        OscServer server;
        IInputController inputController;

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
            inputController = new WinInputController();
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
                inputController.JoypadEvent(btn, depressed);                
            }
            else if (e.Message.Address == MOUSE_EVENT)
            {
                int btn = reverseEndianizeOscInt(e.Message.Data[0]);
                int dx = reverseEndianizeOscInt(e.Message.Data[1]);
                int dy = reverseEndianizeOscInt(e.Message.Data[2]);
                log.Log(LogLevel.Info, string.Format("    btn: {0}, dx: {1}, dy: {2}", btn, dx, dy));
                inputController.MouseEvent(btn, dx, dy);
            }
            else if (e.Message.Address == SCROLL_EVENT)
            {
                int dx = reverseEndianizeOscInt(e.Message.Data[0]);
                int dy = reverseEndianizeOscInt(e.Message.Data[1]);
                log.Log(LogLevel.Info, string.Format("    dx: {0}, dy: {1}", dx, dy));
                inputController.ScrollEvent(dx, dy);
            }
            else if (e.Message.Address == KEYBOARD_EVENT)
            {
                int code = reverseEndianizeOscInt(e.Message.Data[0]);
                bool shift = reverseEndianizeOscInt(e.Message.Data[1]) != 0;
                log.Log(LogLevel.Info, string.Format("    code: {0,2:X}, '{1}', shift: {2}", code, (char)code, shift));
                inputController.KeyboardEvent(code, shift);
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
                            inputController.SysKeyDown((int)ks.Key);
                        else
                            inputController.SysKeyUp((int)ks.Key);
                    }
                }
                else
                {
                    if (d == 1)
                        inputController.VolumeUp();
                    else
                        inputController.VolumeDown();
                }
            }
        }
    }
}
