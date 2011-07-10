package com.djpsoft.remote;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import android.util.Log;

public class Wol {
    private static final String TAG = "Wol";

    /**
     * Try to extract a hardware MAC address from a given IP address using the
     * ARP cache (/proc/net/arp).<br>
     * <br>
     * We assume that the file has this structure:<br>
     * <br>
     * IP address       HW type     Flags       HW address            Mask     Device
     * 192.168.18.11    0x1         0x2         00:04:20:06:55:1a     *        eth0
     * 192.168.18.36    0x1         0x2         00:22:43:ab:2a:5b     *        eth0
     *
     * @param ip
     * @return the MAC from the ARP cache
     */
    public static String getMacFromArpCache(String ip) {
        if (ip == null)
            return null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("/proc/net/arp"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] splitted = line.split(" +");
                if (splitted != null && splitted.length >= 4 && ip.equals(splitted[0])) {
                    // Basic sanity check
                    String mac = splitted[3];
                    if (mac.matches("..:..:..:..:..:..")) {
                        return mac;
                    } else {
                        return null;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /*
     getHardwareAddress not supported before Android 2.3 :(

    public static String getMacFromIp(InetAddress address) {
        //
        // Get NetworkInterface for the current host and then read the
        // hardware address.
        //
        NetworkInterface ni = NetworkInterface.getByInetAddress(address);
        if (ni != null) {
            byte[] mac = ni.getHardwareAddress();
            if (mac != null) {
                //
                // Extract each array of mac address and convert it to hexa with the
                // following format 08-00-27-DC-4A-9E.
                //
                for (int i = 0; i < mac.length; i++) {
                    System.out.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : "");
                }
             } else {
                System.out.println("Address doesn't exist or is not accessible.");
            }
        } else {
            System.out.println("Network Interface for the specified address is not found.");
        }
    }
    */

    public static void wakeUp(byte[] mac) {
        //
        // WOL packet is sent over UDP 255.255.255.0:40000.
        //
        InetAddress address;
        try {
            address = InetAddress.getByName("255.255.255.0");
        } catch (UnknownHostException e1) {
            Log.w(TAG, "WakeUp() InetAddress.getByName() failed");
            return;
        }
        int port = 40000;

        //
        // WOL packet contains a 6-bytes trailer and 16 times a 6-bytes sequence containing the MAC address.
        //
        byte[] bytes = new byte[17 * 6];

        //
        // Trailer of 6 times 0xFF.
        //
        for (int i = 0; i < 6; i++)
            bytes[i] = (byte) 0xFF;

        //
        // Body of magic packet contains 16 times the MAC address.
        //
        for (int i = 1; i <= 16; i++)
            for (int j = 0; j < 6; j++)
                bytes[i * 6 + j] = mac[j];

        //
        // Submit WOL packet.
        //
        try {
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, port);
            DatagramSocket socket = new DatagramSocket();
            socket.send(packet);
            socket.close();
        } catch (Exception e) {
            Log.w(TAG, "WakeUp() socket.send() failed");
        }
    }

    public static void wakeUp(String macStr) {
        byte[] macBytes = getMacBytes(macStr);
        wakeUp(macBytes);
    }

    private static byte[] getMacBytes(String macStr) throws IllegalArgumentException {
        byte[] bytes = new byte[6];
        String[] hex = macStr.split("(\\:|\\-)");
        if (hex.length != 6) {
            throw new IllegalArgumentException("Invalid MAC address.");
        }
        try {
            for (int i = 0; i < 6; i++) {
                bytes[i] = (byte) Integer.parseInt(hex[i], 16);
            }
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid hex digit in MAC address.");
        }
        return bytes;
    }
}
