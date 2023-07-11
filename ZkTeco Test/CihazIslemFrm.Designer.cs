
namespace AttLogs
{
    partial class CihazIslemFrm
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
            this.lblCihazName = new System.Windows.Forms.Label();
            this.groupBox1 = new System.Windows.Forms.GroupBox();
            this.groupBox2 = new System.Windows.Forms.GroupBox();
            this.edtPortNumber = new System.Windows.Forms.TextBox();
            this.listBox1 = new System.Windows.Forms.ListBox();
            this.groupBox3 = new System.Windows.Forms.GroupBox();
            this.checkBox1 = new System.Windows.Forms.CheckBox();
            this.btnCihazKullaniciTanimla = new System.Windows.Forms.Button();
            this.btnCihazVerileriOku = new System.Windows.Forms.Button();
            this.button1 = new System.Windows.Forms.Button();
            this.groupBox4 = new System.Windows.Forms.GroupBox();
            this.textBox2 = new System.Windows.Forms.TextBox();
            this.groupBox1.SuspendLayout();
            this.groupBox2.SuspendLayout();
            this.groupBox3.SuspendLayout();
            this.groupBox4.SuspendLayout();
            this.SuspendLayout();
            // 
            // lblCihazName
            // 
            this.lblCihazName.AutoSize = true;
            this.lblCihazName.BackColor = System.Drawing.SystemColors.Menu;
            this.lblCihazName.Dock = System.Windows.Forms.DockStyle.Bottom;
            this.lblCihazName.Font = new System.Drawing.Font("Microsoft Sans Serif", 16F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(162)));
            this.lblCihazName.ForeColor = System.Drawing.SystemColors.Highlight;
            this.lblCihazName.Location = new System.Drawing.Point(0, 545);
            this.lblCihazName.Margin = new System.Windows.Forms.Padding(3, 0, 3, 3);
            this.lblCihazName.Name = "lblCihazName";
            this.lblCihazName.Padding = new System.Windows.Forms.Padding(4);
            this.lblCihazName.Size = new System.Drawing.Size(242, 34);
            this.lblCihazName.TabIndex = 9;
            this.lblCihazName.Text = "Strike SC 902 Cihazı";
            // 
            // groupBox1
            // 
            this.groupBox1.Controls.Add(this.groupBox2);
            this.groupBox1.Controls.Add(this.listBox1);
            this.groupBox1.Dock = System.Windows.Forms.DockStyle.Left;
            this.groupBox1.Location = new System.Drawing.Point(0, 0);
            this.groupBox1.Name = "groupBox1";
            this.groupBox1.Size = new System.Drawing.Size(297, 545);
            this.groupBox1.TabIndex = 10;
            this.groupBox1.TabStop = false;
            this.groupBox1.Text = "Cihaz Listesi";
            // 
            // groupBox2
            // 
            this.groupBox2.Controls.Add(this.edtPortNumber);
            this.groupBox2.Dock = System.Windows.Forms.DockStyle.Bottom;
            this.groupBox2.Location = new System.Drawing.Point(3, 492);
            this.groupBox2.Name = "groupBox2";
            this.groupBox2.Size = new System.Drawing.Size(291, 50);
            this.groupBox2.TabIndex = 5;
            this.groupBox2.TabStop = false;
            this.groupBox2.Text = "Cihaz Port Numarası";
            // 
            // edtPortNumber
            // 
            this.edtPortNumber.Location = new System.Drawing.Point(6, 19);
            this.edtPortNumber.Name = "edtPortNumber";
            this.edtPortNumber.Size = new System.Drawing.Size(100, 20);
            this.edtPortNumber.TabIndex = 4;
            this.edtPortNumber.Text = "5005";
            // 
            // listBox1
            // 
            this.listBox1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.listBox1.FormattingEnabled = true;
            this.listBox1.IntegralHeight = false;
            this.listBox1.Items.AddRange(new object[] {
            "10.6.240.201",
            "10.6.240.201",
            "10.6.240.201",
            "10.6.240.201",
            "10.6.240.201"});
            this.listBox1.Location = new System.Drawing.Point(3, 16);
            this.listBox1.Name = "listBox1";
            this.listBox1.ScrollAlwaysVisible = true;
            this.listBox1.Size = new System.Drawing.Size(291, 526);
            this.listBox1.TabIndex = 1;
            // 
            // groupBox3
            // 
            this.groupBox3.Controls.Add(this.checkBox1);
            this.groupBox3.Controls.Add(this.btnCihazKullaniciTanimla);
            this.groupBox3.Controls.Add(this.btnCihazVerileriOku);
            this.groupBox3.Controls.Add(this.button1);
            this.groupBox3.Dock = System.Windows.Forms.DockStyle.Top;
            this.groupBox3.Location = new System.Drawing.Point(297, 0);
            this.groupBox3.Name = "groupBox3";
            this.groupBox3.Size = new System.Drawing.Size(832, 58);
            this.groupBox3.TabIndex = 11;
            this.groupBox3.TabStop = false;
            // 
            // checkBox1
            // 
            this.checkBox1.AutoSize = true;
            this.checkBox1.Checked = true;
            this.checkBox1.CheckState = System.Windows.Forms.CheckState.Checked;
            this.checkBox1.Location = new System.Drawing.Point(345, 23);
            this.checkBox1.Name = "checkBox1";
            this.checkBox1.Size = new System.Drawing.Size(80, 17);
            this.checkBox1.TabIndex = 3;
            this.checkBox1.Text = "Seçili Cihaz";
            this.checkBox1.UseVisualStyleBackColor = true;
            // 
            // btnCihazKullaniciTanimla
            // 
            this.btnCihazKullaniciTanimla.Enabled = false;
            this.btnCihazKullaniciTanimla.Location = new System.Drawing.Point(175, 16);
            this.btnCihazKullaniciTanimla.Name = "btnCihazKullaniciTanimla";
            this.btnCihazKullaniciTanimla.Size = new System.Drawing.Size(153, 31);
            this.btnCihazKullaniciTanimla.TabIndex = 2;
            this.btnCihazKullaniciTanimla.Text = "Cihaza Kullanıcı Tanımla";
            this.btnCihazKullaniciTanimla.UseVisualStyleBackColor = true;
            this.btnCihazKullaniciTanimla.Click += new System.EventHandler(this.button3_Click);
            // 
            // btnCihazVerileriOku
            // 
            this.btnCihazVerileriOku.Enabled = false;
            this.btnCihazVerileriOku.Location = new System.Drawing.Point(16, 16);
            this.btnCihazVerileriOku.Name = "btnCihazVerileriOku";
            this.btnCihazVerileriOku.Size = new System.Drawing.Size(153, 31);
            this.btnCihazVerileriOku.TabIndex = 1;
            this.btnCihazVerileriOku.Text = "Giriş - Çıkış Verilerini Oku";
            this.btnCihazVerileriOku.UseVisualStyleBackColor = true;
            this.btnCihazVerileriOku.Click += new System.EventHandler(this.button2_Click);
            // 
            // button1
            // 
            this.button1.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.button1.Location = new System.Drawing.Point(703, 19);
            this.button1.Name = "button1";
            this.button1.Size = new System.Drawing.Size(117, 23);
            this.button1.TabIndex = 0;
            this.button1.Text = "Programı Kapat";
            this.button1.UseVisualStyleBackColor = true;
            this.button1.Click += new System.EventHandler(this.button1_Click);
            // 
            // groupBox4
            // 
            this.groupBox4.Controls.Add(this.textBox2);
            this.groupBox4.Dock = System.Windows.Forms.DockStyle.Fill;
            this.groupBox4.Location = new System.Drawing.Point(297, 58);
            this.groupBox4.Name = "groupBox4";
            this.groupBox4.Size = new System.Drawing.Size(832, 487);
            this.groupBox4.TabIndex = 12;
            this.groupBox4.TabStop = false;
            this.groupBox4.Text = "İletişim Logları";
            // 
            // textBox2
            // 
            this.textBox2.Dock = System.Windows.Forms.DockStyle.Fill;
            this.textBox2.Font = new System.Drawing.Font("Courier New", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(162)));
            this.textBox2.Location = new System.Drawing.Point(3, 16);
            this.textBox2.Multiline = true;
            this.textBox2.Name = "textBox2";
            this.textBox2.ScrollBars = System.Windows.Forms.ScrollBars.Vertical;
            this.textBox2.Size = new System.Drawing.Size(826, 468);
            this.textBox2.TabIndex = 1;
            // 
            // CihazIslemFrm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(1129, 579);
            this.Controls.Add(this.groupBox4);
            this.Controls.Add(this.groupBox3);
            this.Controls.Add(this.groupBox1);
            this.Controls.Add(this.lblCihazName);
            this.Name = "CihazIslemFrm";
            this.Text = "Cihaz Veri Okuma İşlemleri";
            this.Load += new System.EventHandler(this.CihazIslemFrm_Load);
            this.groupBox1.ResumeLayout(false);
            this.groupBox2.ResumeLayout(false);
            this.groupBox2.PerformLayout();
            this.groupBox3.ResumeLayout(false);
            this.groupBox3.PerformLayout();
            this.groupBox4.ResumeLayout(false);
            this.groupBox4.PerformLayout();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Label lblCihazName;
        private System.Windows.Forms.GroupBox groupBox1;
        private System.Windows.Forms.GroupBox groupBox2;
        private System.Windows.Forms.TextBox edtPortNumber;
        private System.Windows.Forms.ListBox listBox1;
        private System.Windows.Forms.GroupBox groupBox3;
        private System.Windows.Forms.GroupBox groupBox4;
        private System.Windows.Forms.Button button1;
        private System.Windows.Forms.TextBox textBox2;
        private System.Windows.Forms.Button btnCihazVerileriOku;
        private System.Windows.Forms.Button btnCihazKullaniciTanimla;
        private System.Windows.Forms.CheckBox checkBox1;
    }
}