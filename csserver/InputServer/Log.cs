using System;
using System.Collections.Generic;
using System.Text;

namespace InputServer
{
    public enum LogLevel
    {
        Info,
        Warning,
        Error
    }

    public delegate void Log(LogLevel level, string msg);

    public interface ILog
    {
        void Log(LogLevel level, string msg);
    }

    public class ConsoleLog : ILog
    {
        public void Log(LogLevel level, string msg)
        {
            Console.WriteLine(msg);
        }
    }
}
