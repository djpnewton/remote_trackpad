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
            IInputController inputController;
            switch (System.Environment.OSVersion.Platform)
            {
                case PlatformID.Win32NT:
                    inputController = new WinInputController();
                    break;
                case PlatformID.Unix:
                    inputController = new LinInputController();
                    break;
                default:
                    Console.WriteLine("Environment not supported");
                    return;
            }

            InputServer.InputServer iserver = new InputServer.InputServer(log, inputController);

            Console.WriteLine("Press any key to exit.");
            Console.ReadKey();

            iserver.Dispose();
            dserver.Dispose();
        }
    }
}
