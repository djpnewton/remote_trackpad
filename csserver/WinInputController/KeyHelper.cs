using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace InputController
{
    public static class KeyHelper
    {
        public struct KeyState
        {
            public Keys Key;
            public bool KeyDown;
            public KeyState(Keys key, bool keyDown)
            {
                Key = key;
                KeyDown = keyDown;
            }
        }

        public static string KeyCodeToString(Keys key, bool keyDown)
        {
            if (keyDown)
                return string.Format(@"[{0}]", (int)key);
            else
                return string.Format(@"<{0}>", (int)key);
        }

        public static List<KeyState> StringToKeyStates(string s)
        {
            List<KeyState> keys = new List<KeyState>();
            bool initial = true;
            bool inDownKey = false;
            bool inUpKey = false;
            string currentInt = "";
            for (int i = 0; i < s.Length; i++)
            {
                if (initial)
                {
                    if (s[i] == '[')
                    {
                        initial = false;
                        inDownKey = true;
                        currentInt = "";
                    }
                    else if (s[i] == '<')
                    {
                        initial = false;
                        inUpKey = true;
                        currentInt = "";
                    }
                }
                else if (inDownKey)
                {
                    if (s[i] != ']')
                        currentInt += s[i];
                    else
                    {
                        initial = true;
                        inDownKey = false;
                        int code;
                        if (int.TryParse(currentInt, out code))
                            keys.Add(new KeyState((Keys)code, true));
                    }
                }
                else if (inUpKey)
                {
                    if (s[i] != '>')
                        currentInt += s[i];
                    else
                    {
                        initial = true;
                        inUpKey = false;
                        int code;
                        if (int.TryParse(currentInt, out code))
                            keys.Add(new KeyState((Keys)code, false));
                    }
                }
            }
            return keys;
        }
    }
}
