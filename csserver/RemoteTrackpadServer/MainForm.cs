using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Net;
using System.Windows.Forms;
using System.Reflection;
using System.Diagnostics;
using InputServer;
using InputController;

namespace RemoteTrackpadServer
{
    public partial class MainForm : Form
    {
#if USELOG
        bool useLog = true;
#else
        bool useLog = false;
#endif

        WinInputController inputController;
        DiscoveryServer dserver;
        InputServer.InputServer iserver;
        LogForm logForm;
        bool loadingPrefs = false;
        bool closeForReal = false;

        public MainForm()
        {
            InitializeComponent();

            // log
            foreach (string arg in Environment.GetCommandLineArgs())
                if (arg == "--log")
                    useLog = true;
            ILog log;
            miLog.Visible = useLog;
            if (useLog)
            {
                logForm = new LogForm();
                log = new TextBoxLog(logForm.TextBox);
            }
            else
                log = new NullLog();

            // version string
            lbVersion.Text = GetVersion();

            // ip address
            try
            {
                lbIpAddr.Text = GetIpAddress();
            }
            catch
            {
                lbIpAddr.Text = "Unknown";
            }

            // input controller
            inputController = new WinInputController();

            // services
            dserver = new DiscoveryServer(log);
            iserver = new InputServer.InputServer(log, inputController);

            // hide window
            WindowState = FormWindowState.Minimized;
            ShowInTaskbar = false;
        }

        private string GetVersion()
        {
            Assembly assembly = Assembly.GetExecutingAssembly();
            FileVersionInfo fvi = FileVersionInfo.GetVersionInfo(assembly.Location);
            return fvi.ProductVersion;
        }

        private void UpdateInputServerConf()
        {
            iserver.UseVolumeMacros = rbTriggerKbMacro.Checked;
            iserver.VolumeUpMacro = tbVolumeUpMacro.Text;
            iserver.VolumeDownMacro = tbVolumeDownMacro.Text;
        }

        void LoadPrefs()
        {
            loadingPrefs = true;
            rbChangePCVolume.Checked = !Properties.Settings.Default.UseVolumeMacros;
            rbTriggerKbMacro.Checked = Properties.Settings.Default.UseVolumeMacros;
            tbVolumeUpMacro.Text = Properties.Settings.Default.VolumeUpMacro;
            tbVolumeDownMacro.Text = Properties.Settings.Default.VolumeDownMacro;
            UpdateInputServerConf();
            loadingPrefs = false;
        }

        void SavePrefs()
        {
            if (!loadingPrefs)
            {
                Properties.Settings.Default.UseVolumeMacros = rbTriggerKbMacro.Checked;
                Properties.Settings.Default.VolumeUpMacro = tbVolumeUpMacro.Text;
                Properties.Settings.Default.VolumeDownMacro = tbVolumeDownMacro.Text;
                Properties.Settings.Default.Save();
            }
        }
 
        private void MainForm_Load(object sender, EventArgs e)
        {
            LoadPrefs();
        }

        private void notifyIcon1_DoubleClick(object sender, EventArgs e)
        {
            // show window
            WindowState = FormWindowState.Normal;
            ShowInTaskbar = true;
        }

        private void contextMenuStrip1_ItemClicked(object sender, ToolStripItemClickedEventArgs e)
        {
            if (e.ClickedItem == miSetup)
            {
                // show window
                WindowState = FormWindowState.Normal;
                ShowInTaskbar = true;
            }
            if (e.ClickedItem == miLog)
            {
                logForm.Show();
            }
            if (e.ClickedItem == miClose)
            {
                closeForReal = true;
                Close();
            }
        }

        private void Form1_FormClosing(object sender, FormClosingEventArgs e)
        {
            if (!closeForReal && e.CloseReason == CloseReason.UserClosing)
            {
                e.Cancel = true;
                // hide window
                WindowState = FormWindowState.Minimized;
                ShowInTaskbar = false;
            }
            else
            {
                iserver.Dispose();
                dserver.Dispose();
            }
        }

        private void contextMenuStrip1_Opening(object sender, CancelEventArgs e)
        {
            miVersion.Text = "Server Version is " + GetVersion();
            try
            {
                miLocalIp.Text = "Server IP address is " + GetIpAddress();
                miLocalIp.Visible = true;
            }
            catch
            {
                miLocalIp.Visible = false;
            }
        }

        private string GetIpAddress()
        {
            IPHostEntry IPHost = Dns.GetHostByName(Dns.GetHostName());
            return IPHost.AddressList[0].ToString();
        }

        private void rbChangePCVolume_CheckedChanged(object sender, EventArgs e)
        {
            UpdateInputServerConf();
            SavePrefs();
        }

        private void rbTriggerKbMacro_CheckedChanged(object sender, EventArgs e)
        {
            UpdateInputServerConf();
            SavePrefs();
        }

        private void tbVolumeUpAction_TextChanged(object sender, EventArgs e)
        {
            UpdateInputServerConf();
            SavePrefs();
        }

        private void tbVolumeDownAction_TextChanged(object sender, EventArgs e)
        {
            UpdateInputServerConf();
            SavePrefs();
        }

        void tbVolumeMacro_KeyPress(object sender, KeyPressEventArgs e)
        {
            e.Handled = true;
        }

        void tbVolumeUpMacro_KeyDown(object sender, KeyEventArgs e)
        {
            tbVolumeUpMacro.Text += inputController.KeyCodeToMacro((int)e.KeyCode, true);
        }

        void tbVolumeUpMacro_KeyUp(object sender, KeyEventArgs e)
        {
            tbVolumeUpMacro.Text += inputController.KeyCodeToMacro((int)e.KeyCode, false);
        }

        void tbVolumeDownMacro_KeyDown(object sender, KeyEventArgs e)
        {
            tbVolumeDownMacro.Text += inputController.KeyCodeToMacro((int)e.KeyCode, true);
        }

        void tbVolumeDownMacro_KeyUp(object sender, KeyEventArgs e)
        {
            tbVolumeDownMacro.Text += inputController.KeyCodeToMacro((int)e.KeyCode, false);
        }

        private void btnClearVolUp_Click(object sender, EventArgs e)
        {
            tbVolumeUpMacro.Text = "";
            tbVolumeUpMacro.Focus();
        }

        private void btnClearVolDown_Click(object sender, EventArgs e)
        {
            tbVolumeDownMacro.Text = "";
            tbVolumeDownMacro.Focus();
        }
    }
}
