using System;
using Gtk;
using InputServer;
using InputController;
using RemoteTrackpadServerGtk;

public partial class MainWindow : Gtk.Window
{
    IInputController inputController;
    DiscoveryServer dserver;
    InputServer.InputServer iserver;

    public MainWindow () : base(Gtk.WindowType.Toplevel)
    {
        Build ();

        // log
        ILog log = new TextViewLog(this.textview1);

        // input controller
        inputController = new LinInputController();

        // services
        dserver = new DiscoveryServer(log);
        iserver = new InputServer.InputServer(log, inputController);
    }

    protected void OnDeleteEvent (object sender, DeleteEventArgs a)
    {
        iserver.Dispose();
        dserver.Dispose();

        Application.Quit ();
        a.RetVal = true;
    }
}

