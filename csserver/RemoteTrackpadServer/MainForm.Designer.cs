namespace RemoteTrackpadServer
{
    partial class MainForm
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.components = new System.ComponentModel.Container();
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(MainForm));
            this.notifyIcon1 = new System.Windows.Forms.NotifyIcon(this.components);
            this.contextMenuStrip1 = new System.Windows.Forms.ContextMenuStrip(this.components);
            this.miLocalIp = new System.Windows.Forms.ToolStripMenuItem();
            this.miSetup = new System.Windows.Forms.ToolStripMenuItem();
            this.miLog = new System.Windows.Forms.ToolStripMenuItem();
            this.miClose = new System.Windows.Forms.ToolStripMenuItem();
            this.gbVolumeButtons = new System.Windows.Forms.GroupBox();
            this.rbTriggerKbMacro = new System.Windows.Forms.RadioButton();
            this.rbChangePCVolume = new System.Windows.Forms.RadioButton();
            this.btnClearVolDown = new System.Windows.Forms.Button();
            this.btnClearVolUp = new System.Windows.Forms.Button();
            this.label2 = new System.Windows.Forms.Label();
            this.label1 = new System.Windows.Forms.Label();
            this.tbVolumeDownMacro = new System.Windows.Forms.TextBox();
            this.tbVolumeUpMacro = new System.Windows.Forms.TextBox();
            this.gbVersion = new System.Windows.Forms.GroupBox();
            this.lbVersion = new System.Windows.Forms.Label();
            this.miVersion = new System.Windows.Forms.ToolStripMenuItem();
            this.gbIpAddr = new System.Windows.Forms.GroupBox();
            this.lbIpAddr = new System.Windows.Forms.Label();
            this.contextMenuStrip1.SuspendLayout();
            this.gbVolumeButtons.SuspendLayout();
            this.gbVersion.SuspendLayout();
            this.gbIpAddr.SuspendLayout();
            this.SuspendLayout();
            // 
            // notifyIcon1
            // 
            this.notifyIcon1.ContextMenuStrip = this.contextMenuStrip1;
            this.notifyIcon1.Icon = ((System.Drawing.Icon)(resources.GetObject("notifyIcon1.Icon")));
            this.notifyIcon1.Text = "Input Server";
            this.notifyIcon1.Visible = true;
            this.notifyIcon1.DoubleClick += new System.EventHandler(this.notifyIcon1_DoubleClick);
            // 
            // contextMenuStrip1
            // 
            this.contextMenuStrip1.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.miVersion,
            this.miLocalIp,
            this.miSetup,
            this.miLog,
            this.miClose});
            this.contextMenuStrip1.Name = "contextMenuStrip1";
            this.contextMenuStrip1.Size = new System.Drawing.Size(144, 124);
            this.contextMenuStrip1.Opening += new System.ComponentModel.CancelEventHandler(this.contextMenuStrip1_Opening);
            this.contextMenuStrip1.ItemClicked += new System.Windows.Forms.ToolStripItemClickedEventHandler(this.contextMenuStrip1_ItemClicked);
            // 
            // miLocalIp
            // 
            this.miLocalIp.Name = "miLocalIp";
            this.miLocalIp.Size = new System.Drawing.Size(143, 24);
            this.miLocalIp.Text = "ipaddr";
            // 
            // miSetup
            // 
            this.miSetup.Name = "miSetup";
            this.miSetup.Size = new System.Drawing.Size(143, 24);
            this.miSetup.Text = "Setup";
            // 
            // miLog
            // 
            this.miLog.Name = "miLog";
            this.miLog.Size = new System.Drawing.Size(143, 24);
            this.miLog.Text = "Show Log";
            // 
            // miClose
            // 
            this.miClose.Name = "miClose";
            this.miClose.Size = new System.Drawing.Size(143, 24);
            this.miClose.Text = "Close";
            // 
            // gbVolumeButtons
            // 
            this.gbVolumeButtons.Controls.Add(this.rbTriggerKbMacro);
            this.gbVolumeButtons.Controls.Add(this.rbChangePCVolume);
            this.gbVolumeButtons.Controls.Add(this.btnClearVolDown);
            this.gbVolumeButtons.Controls.Add(this.btnClearVolUp);
            this.gbVolumeButtons.Controls.Add(this.label2);
            this.gbVolumeButtons.Controls.Add(this.label1);
            this.gbVolumeButtons.Controls.Add(this.tbVolumeDownMacro);
            this.gbVolumeButtons.Controls.Add(this.tbVolumeUpMacro);
            this.gbVolumeButtons.Location = new System.Drawing.Point(13, 145);
            this.gbVolumeButtons.Margin = new System.Windows.Forms.Padding(4, 4, 4, 4);
            this.gbVolumeButtons.Name = "gbVolumeButtons";
            this.gbVolumeButtons.Padding = new System.Windows.Forms.Padding(4, 4, 4, 4);
            this.gbVolumeButtons.Size = new System.Drawing.Size(489, 151);
            this.gbVolumeButtons.TabIndex = 2;
            this.gbVolumeButtons.TabStop = false;
            this.gbVolumeButtons.Text = "Volume Buttons";
            // 
            // rbTriggerKbMacro
            // 
            this.rbTriggerKbMacro.AutoSize = true;
            this.rbTriggerKbMacro.Location = new System.Drawing.Point(12, 52);
            this.rbTriggerKbMacro.Margin = new System.Windows.Forms.Padding(4, 4, 4, 4);
            this.rbTriggerKbMacro.Name = "rbTriggerKbMacro";
            this.rbTriggerKbMacro.Size = new System.Drawing.Size(187, 21);
            this.rbTriggerKbMacro.TabIndex = 1;
            this.rbTriggerKbMacro.TabStop = true;
            this.rbTriggerKbMacro.Text = "Trigger Keyboard Macro:";
            this.rbTriggerKbMacro.UseVisualStyleBackColor = true;
            this.rbTriggerKbMacro.CheckedChanged += new System.EventHandler(this.rbTriggerKbMacro_CheckedChanged);
            // 
            // rbChangePCVolume
            // 
            this.rbChangePCVolume.AutoSize = true;
            this.rbChangePCVolume.Location = new System.Drawing.Point(12, 23);
            this.rbChangePCVolume.Margin = new System.Windows.Forms.Padding(4, 4, 4, 4);
            this.rbChangePCVolume.Name = "rbChangePCVolume";
            this.rbChangePCVolume.Size = new System.Drawing.Size(151, 21);
            this.rbChangePCVolume.TabIndex = 0;
            this.rbChangePCVolume.TabStop = true;
            this.rbChangePCVolume.Text = "Change PC Volume";
            this.rbChangePCVolume.UseVisualStyleBackColor = true;
            this.rbChangePCVolume.CheckedChanged += new System.EventHandler(this.rbChangePCVolume_CheckedChanged);
            // 
            // btnClearVolDown
            // 
            this.btnClearVolDown.Location = new System.Drawing.Point(403, 111);
            this.btnClearVolDown.Margin = new System.Windows.Forms.Padding(4, 4, 4, 4);
            this.btnClearVolDown.Name = "btnClearVolDown";
            this.btnClearVolDown.Size = new System.Drawing.Size(79, 28);
            this.btnClearVolDown.TabIndex = 7;
            this.btnClearVolDown.Text = "Clear";
            this.btnClearVolDown.UseVisualStyleBackColor = true;
            this.btnClearVolDown.Click += new System.EventHandler(this.btnClearVolDown_Click);
            // 
            // btnClearVolUp
            // 
            this.btnClearVolUp.Location = new System.Drawing.Point(403, 79);
            this.btnClearVolUp.Margin = new System.Windows.Forms.Padding(4, 4, 4, 4);
            this.btnClearVolUp.Name = "btnClearVolUp";
            this.btnClearVolUp.Size = new System.Drawing.Size(79, 28);
            this.btnClearVolUp.TabIndex = 4;
            this.btnClearVolUp.Text = "Clear";
            this.btnClearVolUp.UseVisualStyleBackColor = true;
            this.btnClearVolUp.Click += new System.EventHandler(this.btnClearVolUp_Click);
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(27, 117);
            this.label2.Margin = new System.Windows.Forms.Padding(4, 0, 4, 0);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(141, 17);
            this.label2.TabIndex = 5;
            this.label2.Text = "Volume Down Macro:";
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(27, 85);
            this.label1.Margin = new System.Windows.Forms.Padding(4, 0, 4, 0);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(124, 17);
            this.label1.TabIndex = 3;
            this.label1.Text = "Volume Up Macro:";
            // 
            // tbVolumeDownMacro
            // 
            this.tbVolumeDownMacro.Location = new System.Drawing.Point(180, 113);
            this.tbVolumeDownMacro.Margin = new System.Windows.Forms.Padding(4, 4, 4, 4);
            this.tbVolumeDownMacro.Name = "tbVolumeDownMacro";
            this.tbVolumeDownMacro.Size = new System.Drawing.Size(213, 22);
            this.tbVolumeDownMacro.TabIndex = 6;
            this.tbVolumeDownMacro.TextChanged += new System.EventHandler(this.tbVolumeDownAction_TextChanged);
            this.tbVolumeDownMacro.KeyDown += new System.Windows.Forms.KeyEventHandler(this.tbVolumeDownMacro_KeyDown);
            this.tbVolumeDownMacro.KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.tbVolumeMacro_KeyPress);
            this.tbVolumeDownMacro.KeyUp += new System.Windows.Forms.KeyEventHandler(this.tbVolumeDownMacro_KeyUp);
            // 
            // tbVolumeUpMacro
            // 
            this.tbVolumeUpMacro.Location = new System.Drawing.Point(180, 81);
            this.tbVolumeUpMacro.Margin = new System.Windows.Forms.Padding(4, 4, 4, 4);
            this.tbVolumeUpMacro.Name = "tbVolumeUpMacro";
            this.tbVolumeUpMacro.Size = new System.Drawing.Size(213, 22);
            this.tbVolumeUpMacro.TabIndex = 3;
            this.tbVolumeUpMacro.TextChanged += new System.EventHandler(this.tbVolumeUpAction_TextChanged);
            this.tbVolumeUpMacro.KeyDown += new System.Windows.Forms.KeyEventHandler(this.tbVolumeUpMacro_KeyDown);
            this.tbVolumeUpMacro.KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.tbVolumeMacro_KeyPress);
            this.tbVolumeUpMacro.KeyUp += new System.Windows.Forms.KeyEventHandler(this.tbVolumeUpMacro_KeyUp);
            // 
            // gbVersion
            // 
            this.gbVersion.Controls.Add(this.lbVersion);
            this.gbVersion.Location = new System.Drawing.Point(13, 12);
            this.gbVersion.Name = "gbVersion";
            this.gbVersion.Size = new System.Drawing.Size(489, 60);
            this.gbVersion.TabIndex = 3;
            this.gbVersion.TabStop = false;
            this.gbVersion.Text = "Server Version";
            // 
            // lbVersion
            // 
            this.lbVersion.AutoSize = true;
            this.lbVersion.Location = new System.Drawing.Point(9, 28);
            this.lbVersion.Name = "lbVersion";
            this.lbVersion.Size = new System.Drawing.Size(54, 17);
            this.lbVersion.TabIndex = 0;
            this.lbVersion.Text = "version";
            // 
            // miVersion
            // 
            this.miVersion.Name = "miVersion";
            this.miVersion.Size = new System.Drawing.Size(143, 24);
            this.miVersion.Text = "version";
            // 
            // gbIpAddr
            // 
            this.gbIpAddr.Controls.Add(this.lbIpAddr);
            this.gbIpAddr.Location = new System.Drawing.Point(12, 78);
            this.gbIpAddr.Name = "gbIpAddr";
            this.gbIpAddr.Size = new System.Drawing.Size(489, 60);
            this.gbIpAddr.TabIndex = 4;
            this.gbIpAddr.TabStop = false;
            this.gbIpAddr.Text = "Server IP Address";
            // 
            // lbIpAddr
            // 
            this.lbIpAddr.AutoSize = true;
            this.lbIpAddr.Location = new System.Drawing.Point(9, 28);
            this.lbIpAddr.Name = "lbIpAddr";
            this.lbIpAddr.Size = new System.Drawing.Size(48, 17);
            this.lbIpAddr.TabIndex = 0;
            this.lbIpAddr.Text = "ipaddr";
            // 
            // MainForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 16F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(516, 309);
            this.Controls.Add(this.gbIpAddr);
            this.Controls.Add(this.gbVersion);
            this.Controls.Add(this.gbVolumeButtons);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedDialog;
            this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
            this.Margin = new System.Windows.Forms.Padding(3, 2, 3, 2);
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "MainForm";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "Remote Trackpad Server Setup";
            this.FormClosing += new System.Windows.Forms.FormClosingEventHandler(this.Form1_FormClosing);
            this.Load += new System.EventHandler(this.MainForm_Load);
            this.contextMenuStrip1.ResumeLayout(false);
            this.gbVolumeButtons.ResumeLayout(false);
            this.gbVolumeButtons.PerformLayout();
            this.gbVersion.ResumeLayout(false);
            this.gbVersion.PerformLayout();
            this.gbIpAddr.ResumeLayout(false);
            this.gbIpAddr.PerformLayout();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.NotifyIcon notifyIcon1;
        private System.Windows.Forms.ContextMenuStrip contextMenuStrip1;
        private System.Windows.Forms.ToolStripMenuItem miClose;
        private System.Windows.Forms.ToolStripMenuItem miLocalIp;
        private System.Windows.Forms.ToolStripMenuItem miSetup;
        private System.Windows.Forms.ToolStripMenuItem miLog;
        private System.Windows.Forms.GroupBox gbVolumeButtons;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.TextBox tbVolumeDownMacro;
        private System.Windows.Forms.TextBox tbVolumeUpMacro;
        private System.Windows.Forms.Button btnClearVolDown;
        private System.Windows.Forms.Button btnClearVolUp;
        private System.Windows.Forms.RadioButton rbTriggerKbMacro;
        private System.Windows.Forms.RadioButton rbChangePCVolume;
        private System.Windows.Forms.GroupBox gbVersion;
        private System.Windows.Forms.Label lbVersion;
        private System.Windows.Forms.ToolStripMenuItem miVersion;
        private System.Windows.Forms.GroupBox gbIpAddr;
        private System.Windows.Forms.Label lbIpAddr;
    }
}

