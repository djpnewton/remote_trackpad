﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using InputServer;

namespace InputServer
{
    public class NullLog : ILog
    {
        public void Log(LogLevel level, string msg)
        {
            // do nothing
        }
    }
}
