using System;
using System.Collections.Generic;
using System.Text;
using System.Windows.Forms;
using InputServer;

namespace RemoteTrackpadServer
{
    public class TextBoxLog : ILog
    {
        TextBox tb;

        public TextBoxLog(TextBox tb)
        {
            this.tb = tb;
        }

        public void Log(LogLevel level, string msg)
        {
            if (tb.InvokeRequired)
                tb.Invoke(new Log(Log), new Object[] { level, msg });
            else
            {
                tb.AppendText(msg + "\r\n");
            }
        }
    }
}
