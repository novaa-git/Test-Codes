using System;
using System.Windows.Forms;
using System.IO;

namespace AttLogs
{
    public partial class CihazIslemFrm : Form
    {
        private int logCount = 0;
        public CihazIslemFrm()
        {
            InitializeComponent();
        }

        private void cihazIpListesiYukle()
        {
            listBox1.Items.Clear();
            string vstrFileName = Directory.GetCurrentDirectory() + "\\iplist.txt";
            if (File.Exists(vstrFileName))
            {
                string[] iplist = File.ReadAllText(vstrFileName).Split(new Char[] { '\n' });
                for (int i = 0; i < iplist.Length; i++)
                {
                    listBox1.Items.Add(iplist[i]);
                }
                listBox1.SelectedIndex = 0;
                addLog("Toplam " + iplist.Length + " ip adresi okundu");
                btnCihazVerileriOku.Enabled = true;
                btnCihazKullaniciTanimla.Enabled = true;
            }
            else
                addLog(vstrFileName + " dosyası (cihaz ip listesi) bulunamadı");
        }

        private void sisoftAdresDosyasiYukle()
        {
            string vstrFileName = Directory.GetCurrentDirectory() + "\\sisoftayar.txt";
            if (File.Exists(vstrFileName))
            {
                string[] ayarlist = File.ReadAllText(vstrFileName).Split(new Char[] { '\n' });
                if (ayarlist.Length > 0)
                {
                    httpBaglantilari.sisoftWebAdress = ayarlist[0].Trim();
                    addLog("Sisoft Bağlantı Adresi : " + httpBaglantilari.sisoftWebAdress);
                    if (ayarlist.Length == 2)
                    {
                        addLog("Cihaz Port Numarası : " + ayarlist[1].Trim());
                        edtPortNumber.Text = ayarlist[1];
                    }

                        
                }
                    
                
            }
            else
                addLog(vstrFileName + " Sisoft Bağlantı Ayar Dosyası Bulunamadı");
        }

        private void CihazIslemFrm_Load(object sender, EventArgs e)
        {
            lblCihazName.Text = "ZKTeco PDKS İletişim Cihazı";
            this.cihazIpListesiYukle();
            this.sisoftAdresDosyasiYukle();
        }

        private void addLog(string log)
        {
            if (logCount > 3000)
            {
                textBox2.Clear();
            }
            log = log.Replace("\n", "");
            textBox2.Text += log + "\r\n";
            textBox2.Refresh();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        private void button2_Click(object sender, EventArgs e)
        {
            try
            {
                btnCihazVerileriOku.Enabled = false;
                string ipadres = listBox1.Items[listBox1.SelectedIndex].ToString();
                if (this.checkBox1.Checked)
                {
                    this.zkErisimVerileriOku(ipadres.Trim(), edtPortNumber.Text);
                } else
                {
                    listBox1.SelectedIndex = 0;
                    for (int i=0; i < listBox1.Items.Count; i++)
                    {
                        ipadres = listBox1.Items[i].ToString();
                        this.zkErisimVerileriOku(ipadres.Trim(), edtPortNumber.Text);
                    }
                }
            }
            finally {
                addLog("< okuma işlemleri tamamlandı > ");
                btnCihazVerileriOku.Enabled = true;
            }

        }

        

        private void button3_Click(object sender, EventArgs e)
        {
            try
            {
                btnCihazKullaniciTanimla.Enabled = false;
                string ipadres = listBox1.Items[listBox1.SelectedIndex].ToString();
                if (this.checkBox1.Checked)
                {
                    this.zkKullanicilariYaz(ipadres.Trim(), edtPortNumber.Text);
                }
                else
                {
                    listBox1.SelectedIndex = 0;
                    for (int i = 0; i < listBox1.Items.Count; i++)
                    {
                        ipadres = listBox1.Items[i].ToString();
                        this.zkKullanicilariYaz(ipadres.Trim(), edtPortNumber.Text);
                    }
                }
            }
            finally
            {
                addLog("< yazma işlemleri tamamlandı > ");
                btnCihazKullaniciTanimla.Enabled = true;
            }
        }


        private void zkErisimVerileriOku(string ipadres, string port)
        {
            addLog("");
            addLog("< Cihaz veri okuma işlemleri başlatılıyor > <"+ ipadres + "@" + port + ">");
            CihazIslemleri ci = new CihazIslemleri();
            ci.setInitConnect(ipadres, Int32.Parse(port) , textBox2);
            ci.setConnect();
            //ci.userWriteExecute("8", "489747047", "serkan test");
            //ci.logReadExecute();
            ci.setDisconnect();
        }

        private void zkKullanicilariYaz(string ipadres, string port)
        {
            addLog("");
            addLog("< Cihaz kullanıcı kayıt işlemleri başlatılıyor > <" + ipadres + ">");
            CihazIslemleri ci = new CihazIslemleri();
            ci.setInitConnect(ipadres, Int32.Parse(port), textBox2);
            ci.kullanicilariWebdenOkuYaz(ipadres);
            ci.setDisconnect();

        }

    }
}
