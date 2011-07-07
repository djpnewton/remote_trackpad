using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace RemoteTrackpadServer
{
    public partial class LogForm : Form
    {
        public TextBox TextBox
        {
            get { return textBox1; }
        }

        public LogForm()
        {
            InitializeComponent();
        }

        private void LogForm_FormClosing(object sender, FormClosingEventArgs e)
        {
            e.Cancel = true;
            Hide();
        }
    }
}
