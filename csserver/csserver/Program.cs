using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using InputServer;
using InputController;

namespace csserver
{
    class Program
    {
        static void Main(string[] args)
        {
            ConsoleLog log = new ConsoleLog();
            DiscoveryServer dserver = new DiscoveryServer(log);
            InputServer.InputServer iserver = new InputServer.InputServer(log, new WinInputController());

            Console.WriteLine("Press any key to exit.");
            Console.ReadKey();

            iserver.Dispose();
            dserver.Dispose();
        }
    }
}
