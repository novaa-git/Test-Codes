using System;
using System.Text;
using System.Windows.Forms;
using System.IO;

namespace AttLogs
{
    public class CihazIslemleri
    {
        public zkemkeeper.CZKEMClass axCZKEM1 = new zkemkeeper.CZKEMClass();
        public Boolean bIsConnected = false;
        private int iMachineNumber = 1;
        private string readAccessLogFile = null;
        private string ipAdresi = "";
        private int portNumarasi = 2473;
        private TextBox logBox = null;

        public void setInitConnect(string ip, int port, TextBox logEdit)
        {
            this.ipAdresi = ip;
            this.portNumarasi = port;
            this.readAccessLogFile = null;
            this.logBox = logEdit;
        }

        public void addLog(string log)
        {
            Console.WriteLine(log);
            if (this.logBox != null)
            {
                logBox.Text += log + "\r\n";
                logBox.Refresh();
            }
        }

        public Boolean setConnect()
        {
            addLog(ipAdresi + "@" + portNumarasi + " bağlantısı oluşturuluyor...");

            bIsConnected = axCZKEM1.Connect_Net(ipAdresi, portNumarasi);
            if (bIsConnected)
            {
                addLog("connect:" + ipAdresi + "@" + portNumarasi);
                axCZKEM1.RegEvent(iMachineNumber, 65535);
            }
            else
            {
                int idwErrorCode = 0;
                axCZKEM1.GetLastError(ref idwErrorCode);
                addLog("Bağlantı aşamasında hata oluştu : ");
                addLog("Unable to connect the device, ErrorCode=" + idwErrorCode.ToString() + " " + ipAdresi + "@" + portNumarasi);
            }
            return bIsConnected;
        }

        public void setDisconnect()
        {
            axCZKEM1.Disconnect();
            addLog("disconnect:" + this.ipAdresi);
        }

        /**
         okunan kayıtları ipadresi.port.log birleşiminden oluşan bir dosyaya kayıt ediliyor.
        */
        private void addLogAccessRead(string userid, string kartno, string tarih, string verify, string inoutmode)
        {
            if (readAccessLogFile == null)
            {
                readAccessLogFile = @Directory.GetCurrentDirectory() + "\\" + this.ipAdresi + "." + this.portNumarasi + ".log";
                if (Directory.Exists(readAccessLogFile) == true)
                {
                    File.Delete(readAccessLogFile);
                }
                using (FileStream fs = File.Create(readAccessLogFile)) { };
            }

            DateTime adwDate = DateTime.Parse(tarih);
            string log = tarih + "^" + userid + "^" + verify + "^" + inoutmode + "^" + adwDate.Year + "^" + adwDate.Month + "^" + adwDate.Day + "^" + adwDate.Hour + "^" + adwDate.Minute + "^" + adwDate.Second + "^" + "0" + "^" + "0" + "^" + this.ipAdresi + "^" + kartno + Convert.ToChar(13) + Convert.ToChar(10) ;
            addLog("Giriş - Çıkış Kaydı Okundu:" + log);
            using (FileStream fs = File.OpenWrite(readAccessLogFile))
            {
                byte[] newBytes = new ASCIIEncoding().GetBytes(log.ToCharArray());
                fs.Seek(0, SeekOrigin.End);
                fs.Write(newBytes, 0, log.Length);
                fs.Close();
            }
        }

        public void logReadExecute()
        {
            if (bIsConnected == false)
            {
                addLog("Please connect the device first");
                return;
            }
            int idwErrorCode = 0;
            int idwEnrollNumber = 0;
            int idwVerifyMode = 0;
            int idwInOutMode = 0;
            string sTime = "";
            int iGLCount = 0;
            int iIndex = 0;
            
            axCZKEM1.EnableDevice(iMachineNumber, false);//disable the device
            if (axCZKEM1.ReadGeneralLogData(iMachineNumber))//read the records to the memory
            {
                while (axCZKEM1.GetGeneralLogDataStr(iMachineNumber, ref idwEnrollNumber, ref idwVerifyMode, ref idwInOutMode, ref sTime)) //get the records from memory
                {
                    iGLCount++;
                    string sCardnumber = "";
                    string sname2 = "";
                    axCZKEM1.GetUserInfo(iMachineNumber, idwEnrollNumber, ref sname2, null, 0, true);
                    axCZKEM1.GetStrCardNumber(out sCardnumber);
                    addLogAccessRead(idwEnrollNumber.ToString(), sCardnumber, sTime, idwVerifyMode.ToString(), idwInOutMode.ToString());
                    iIndex++;
                }
            }
            else
            {
                axCZKEM1.GetLastError(ref idwErrorCode);
                if (idwErrorCode != 0)
                {
                    addLog("Reading data from terminal failed,ErrorCode: " + idwErrorCode.ToString());
                }
                else
                {
                    axCZKEM1.GetLastError(ref idwErrorCode);
                    addLog("Operation failed,ErrorCode=" + idwErrorCode.ToString());
                }
            }
            axCZKEM1.EnableDevice(iMachineNumber, true);//enable the device

            if (iGLCount > 0)
            {
                httpBaglantilari hb = new httpBaglantilari();
                String log = hb.dosyaSunucuyaGonder(this.readAccessLogFile);
                addLog(log);
                // buradan bool dönse, dosya ismini göndersek.
                //String log = hb.verileriSunucuyaGonder("",)
                // burada sunucuya kayıt etme.
            }
        }

        public void kullanicilariWebdenOkuYaz(string ip)
        {
            httpBaglantilari hb = new httpBaglantilari();
            hb.setCihazIslemClass(this);
            userTags[] users =  hb.kullanicilariOku(ip); // okuduktan sonra connect yapılacak, yazılacak kullanıcı varsa 
            if (users != null)
            {
                addLog("Toplam " + users.Length + " personel kaydı " + ip + " adresindeki cihaza kayıt edilecek");
                this.setConnect();

                if ((users != null) && (users.Length > 0))
                {
                    for (int i = 0; i < users.Length; i++)
                    {
                        if (i < 10) // todo sil
                            if ((users[i] != null) && (users[i].id != null) && (users[i].kartid != null))
                                userWriteExecute(users[i].id, users[i].kartid, users[i].username);
                    }
                }
            }
        }

        /** Kullanıcıyı kayıt etmek için. */
        public void userWriteExecute(string userid, string kartno, string sname)
        {
            if (bIsConnected == false)
            {
                return;
            }

            int idwErrorCode = 0;
            bool bEnabled = true;
            int idwEnrollNumber = Convert.ToInt32(userid);
            string sName = sname;
            string sPassword = "0";
            int iPrivilege = 0;
            string sCardnumber = kartno;

            axCZKEM1.EnableDevice(iMachineNumber, false);
            axCZKEM1.SetStrCardNumber(sCardnumber);//Before you using function SetUserInfo,set the card number to make sure you can upload it to the device
            if (axCZKEM1.SetUserInfo(iMachineNumber, idwEnrollNumber, sName, sPassword, iPrivilege, bEnabled))//upload the user's information(card number included)
            {
                addLog("Success SetUserInfo, UserID: " + idwEnrollNumber.ToString() + " Privilege:" + iPrivilege.ToString() + " Cardnumber:" + sCardnumber + " Enabled:" + bEnabled.ToString());
            }
            else
            {
                axCZKEM1.GetLastError(ref idwErrorCode);
                addLog("Operation failed, User Name: "+ sName + " UserID:" + idwEnrollNumber.ToString()  + "  Cardnumber:" + sCardnumber +  " ErrorCode=" + idwErrorCode.ToString());
            }
            axCZKEM1.RefreshData(iMachineNumber);//the data in the device should be refreshed
            axCZKEM1.EnableDevice(iMachineNumber, true);
        }
    }
}
