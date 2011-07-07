	package com.djpsoft.remote;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;

import com.djpsoft.remote.GamepadView.JoypadButton;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortOut;

public class OSCClient {

    public static final String VIP_HOST = "VIP_HOST";
    public static final String VIP_PORT = "VIP_PORT";
    public static final int DEFAULT_VIP_PORT = 12321;

    public final String JOYPAD_BUTTON_EVENT = "/joypad_button_event";
    public final String MOUSE_EVENT = "/mouse_event";
    public final String SCROLL_EVENT = "/scroll_event";
    public final String KEYBOARD_EVENT = "/keyboard_event";
    public final String VOLUME_EVENT = "/volume_event";

    OSCPortOut sender;

    public OSCClient(InetAddress addr, int port) throws SocketException {
        sender = new OSCPortOut(addr, port);
    }

    public void close() {
        sender.close();
    }

    @Override
    public void finalize() {
        sender.close();
    }

    public void SendJoypadButtonEvent(JoypadButton button, boolean depressed) throws IOException {
        Object args[] = new Object[2];
        args[0] = button.ordinal();
        args[1] = depressed ? 1 : 0;
        OSCMessage msg = new OSCMessage(JOYPAD_BUTTON_EVENT, args);
        sender.send(msg);
    }

    public void SendMouseEvent(int button, int dx, int dy) throws IOException {
        Object args[] = new Object[3];
        args[0] = button;
        args[1] = dx;
        args[2] = dy;
        OSCMessage msg = new OSCMessage(MOUSE_EVENT, args);
        sender.send(msg);
    }

    public void SendScrollEvent(int dx, int dy) throws IOException {
        Object args[] = new Object[2];
        args[0] = dx;
        args[1] = dy;
        OSCMessage msg = new OSCMessage(SCROLL_EVENT, args);
        sender.send(msg);
    }

    public void SendKeyboardEvent(int code, boolean shift) throws IOException {
        Object args[] = new Object[2];
        args[0] = code;
        args[1] = shift ? 1 : 0;
        OSCMessage msg = new OSCMessage(KEYBOARD_EVENT, args);
        sender.send(msg);
    }

    public void SendVolumeEvent(int d) throws IOException {
        Object args[] = new Object[1];
        args[0] = d;
        OSCMessage msg = new OSCMessage(VOLUME_EVENT, args);
        sender.send(msg);
    }
}
