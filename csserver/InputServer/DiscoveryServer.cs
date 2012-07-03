using System;
using System.Net;
using System.Net.Sockets;
using System.Text;

namespace InputServer
{
    public class DiscoveryServer : IDisposable
    {
        public const String DEFAULT_MULTICAST_ADDR = "224.1.2.1";
        public const int DEFAULT_MULTICAST_PORT = 12421;

        public const String PING = "VIP_Ping";
        public const String PONG = "VIP_Pong_02";

        ILog log;
        UdpClient subscriber;
        bool disposed = false;

        public DiscoveryServer(ILog log, string host, int port)
        {
            this.log = log;
            subscriber = new UdpClient(port);
            IPAddress addr = IPAddress.Parse(host);
            subscriber.JoinMulticastGroup(addr);
            subscriber.BeginReceive(ReceiveCallback, subscriber);
            log.Log(LogLevel.Info, "Starting Discovery Server");
        }

        public DiscoveryServer(ILog log)
            : this(log, DEFAULT_MULTICAST_ADDR, DEFAULT_MULTICAST_PORT)
        {
        }

        public void Dispose()
        {
            disposed = true;
            log.Log(LogLevel.Info, "Stopping Discovery Server");
            subscriber.Close();
        }

        public void ReceiveCallback(IAsyncResult ar)
        {
            if (!disposed)
            {
                IPEndPoint ep = null;
                Byte[] receiveBytes = subscriber.EndReceive(ar, ref ep);
                string receiveString = Encoding.ASCII.GetString(receiveBytes);
                subscriber.BeginReceive(ReceiveCallback, subscriber);

                log.Log(LogLevel.Info, string.Format("Received: '{0}', from: {1}", receiveString, ep));

                if (receiveString == PING)
                {
                    log.Log(LogLevel.Info, string.Format("  Sending reply ('{0}')...", PONG));
                    UdpClient reply = new UdpClient();
                    byte[] dgram = ASCIIEncoding.UTF8.GetBytes(PONG);
                    reply.Send(dgram, dgram.Length, ep);
                }
            }
        }
    }
}
