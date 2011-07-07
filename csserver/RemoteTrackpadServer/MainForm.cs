using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Net;
using System.Windows.Forms;
using InputServer;
using System.Runtime.InteropServices;

namespace RemoteTrackpadServer
{
    public partial class MainForm : Form
    {
#if USELOG
        bool useLog = true;
#else
        bool useLog = false;
#endif

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

            // services
            dserver = new DiscoveryServer(log);
            iserver = new InputServer.InputServer(log);

            // hide window
            WindowState = FormWindowState.Minimized;
            ShowInTaskbar = false;
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
            try
            {
                IPHostEntry IPHost = Dns.GetHostByName(Dns.GetHostName());
                miLocalIp.Text = "My IP address is " + IPHost.AddressList[0].ToString();
                miLocalIp.Visible = true;
            }
            catch
            {
                miLocalIp.Visible = false;
            }
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
            tbVolumeUpMacro.Text += KeyHelper.KeyCodeToString(e.KeyCode, true);
        }

        void tbVolumeUpMacro_KeyUp(object sender, KeyEventArgs e)
        {
            tbVolumeUpMacro.Text += KeyHelper.KeyCodeToString(e.KeyCode, false);
        }

        void tbVolumeDownMacro_KeyDown(object sender, KeyEventArgs e)
        {
            tbVolumeDownMacro.Text += KeyHelper.KeyCodeToString(e.KeyCode, true);
        }

        void tbVolumeDownMacro_KeyUp(object sender, KeyEventArgs e)
        {
            tbVolumeDownMacro.Text += KeyHelper.KeyCodeToString(e.KeyCode, false);
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
