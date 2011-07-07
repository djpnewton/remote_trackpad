package com.djpsoft.remote;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;
import android.util.Log;

public class OSCDiscover {

    private static final String TAG = "OSCDiscover";

    Context context;
    String host;
    int port;

    public static final String DEFAULT_MULTICAST_ADDR = "224.1.2.1";
    public static final int    DEFAULT_MULTICAST_PORT = 12421;
    public static final String PING = "VIP_Ping";
    public static final String PONG = "VIP_Pong";
    public static final int    DEFAULT_TIMEOUT = 1000;

    public OSCDiscover(Context context, String host, int port) {
        this.context = context;
        this.host = host;
        this.port = port;
    }

    public boolean isWifiEnabled() {
        WifiManager mgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return mgr.isWifiEnabled();
    }

    public boolean enableWifi() {
        WifiManager mgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return mgr.setWifiEnabled(true);
    }

    public static boolean checkHandshake(String host, int port) {
        try {
            InetAddress addr = InetAddress.getByName(host);
            DatagramSocket socket = new DatagramSocket(port);
            // send ping
            byte[] byteArray = PING.getBytes();
            DatagramPacket packet = new DatagramPacket(byteArray, byteArray.length, addr, port);
            socket.send(packet);
            // listen for pong
            byteArray = new byte[PONG.length()];
            packet = new DatagramPacket(byteArray, byteArray.length);
            socket.setSoTimeout(DEFAULT_TIMEOUT);
            try {
                socket.receive(packet);
            }
            catch (SocketTimeoutException e) {
            }
            finally {
                socket.close();
            }
            String response = new String(packet.getData());
            return response.equals(PONG);
        }
        catch (IOException e) {
            Log.e(TAG, "checkHandshake: IOException");
        }
        return false;
    }

    public String multicastPing() {
        String foundHost = "";

        MulticastLock multicastLock = ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).createMulticastLock("multicastLockTagBlahBlah");
        multicastLock.setReferenceCounted(true);
        multicastLock.acquire();

        try {
            // create socket
            InetAddress addr = InetAddress.getByName(host);
            MulticastSocket socket = new MulticastSocket(port);
            socket.joinGroup(addr);
            //socket.setLoopbackMode(true); not working (see http://stackoverflow.com/questions/6325247/multicast-ping-echoing-back-to-my-client)
            // send multicast ping
            byte[] byteArray = PING.getBytes();
            DatagramPacket packet =
                new DatagramPacket(byteArray, byteArray.length, addr, port);
            socket.send(packet);
            // listen for pong
            byteArray = new byte[PONG.length()];
            packet = new DatagramPacket(byteArray, byteArray.length);
            socket.setSoTimeout(DEFAULT_TIMEOUT);
            try {
                socket.receive(packet);
                socket.receive(packet); // need to do this twice :( (see http://stackoverflow.com/questions/6325247/multicast-ping-echoing-back-to-my-client)
            }
            catch (SocketTimeoutException e) {
            }
            finally {
                socket.close();
            }
            String response = new String(packet.getData());
            if (response.equals(PONG)) {
                Log.i(TAG, "multicastPing: Received '" + response +
                        "' from " + packet.getAddress());
                foundHost = packet.getAddress().getHostAddress();
                String localHost = getIpAddressString();
                if (foundHost.equals(localHost)) {
                    Log.e(TAG, "multicastPing: Damn foundHost.equals(localHost)");
                    foundHost = "";
                }
            }
        }
        catch (IOException e) {
            Log.e(TAG, "multicastPing: IOException");
        }

        multicastLock.release();

        return foundHost;
    }

    public int getIpAddress() {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.getIpAddress();
    }

    public String getIpAddressString() {
        int ip = getIpAddress();
        return String.format(
                "%d.%d.%d.%d",
                (ip & 0xff),
                (ip >> 8 & 0xff),
                (ip >> 16 & 0xff),
                (ip >> 24 & 0xff));
    }

    public String bruteforcePing() {
        String foundHost = "";

        try {
            // create socket
            DatagramSocket socket = new DatagramSocket(port);
            // find local ip address range
            int myIp = getIpAddress();
            int ip = myIp & 0x00ffffff;
            // send pings
            DatagramPacket packet;
            byte[] byteArray = PING.getBytes();
            for (int i = 1; i <= 255; i++) {
                int part4 = ip >> 24 & 0xff;
                part4++;
                ip = (myIp & 0x00ffffff) | part4 << 24;
                String ipString = String.format(
                        "%d.%d.%d.%d",
                        (ip & 0xff),
                        (ip >> 8 & 0xff),
                        (ip >> 16 & 0xff),
                        (ip >> 24 & 0xff));
                if (ip != myIp) {
                    InetAddress addr = InetAddress.getByName(ipString);
                    packet = new DatagramPacket(byteArray, byteArray.length, addr, port);
                    socket.send(packet);
                }
                else {
                    Log.i(TAG, "bruteforcePing: skipping my IP " + ipString);
                }
            }
            // listen for pong
            byteArray = new byte[PONG.length()];
            packet = new DatagramPacket(byteArray, byteArray.length);
            socket.setSoTimeout(DEFAULT_TIMEOUT);
            try {
                socket.receive(packet);
            }
            catch (SocketTimeoutException e) {
            }
            finally {
                socket.close();
            }
            String response = new String(packet.getData());
            if (response.equals(PONG)) {
                Log.i(TAG, "bruteforcePing: Received '" + response +
                    "' from " + packet.getAddress());
                foundHost = packet.getAddress().getHostAddress();
            }
        }
        catch (IOException e) {
            Log.e(TAG, "bruteforcePing: IOException");
        }

        return foundHost;
    }
}
