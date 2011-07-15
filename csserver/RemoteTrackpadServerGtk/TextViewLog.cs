using System;
using Gtk;
using InputServer;

namespace RemoteTrackpadServerGtk
{
    public class TextViewLog : ILog
    {
        TextView tv;

        public TextViewLog(TextView tv)
        {
            this.tv = tv;
        }

        public void Log(LogLevel level, string msg)
        {
            tv.Buffer.Insert(tv.Buffer.EndIter, msg + "\n");
        }
    }
}

