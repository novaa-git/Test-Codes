using System;
using System.Text;
using System.Net.Http;
using System.Net;
using System.IO;

namespace AttLogs
{
    public class httpBaglantilari
    {
        public static String sisoftWebAdress = "";
        private CihazIslemleri cihazIslem;
        

        public void setCihazIslemClass(CihazIslemleri ci)
        {
            this.cihazIslem = ci;
        }

        public string verileriSunucuyaGonder(String veri)
        {
            string log = "";

            using (HttpClient client = new HttpClient())
            {
                //"http://10.6.240.153:8081/cozumhbys/GetPatientQuery?requestType=pdksoku"
                log = "veriler sisoft sunucuya gonderiliyor..";
                string urlEk = httpBaglantilari.sisoftWebAdress + "requestType=pdksoku";
                HttpRequestMessage requestMessage = new HttpRequestMessage(HttpMethod.Post, urlEk);
                requestMessage.Content = new StringContent(veri, Encoding.UTF8, "application/text");
                HttpResponseMessage response = client.SendAsync(requestMessage).Result;
                
                if (response.StatusCode == System.Net.HttpStatusCode.OK)
                {
                    string responseString = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
                    log += "\r\n" + " gonderim tamamlandi. islem başarılı, dönüş mesajı:" + responseString;
                } else
                    log += "\r\n" + " gonderim tamamlandi. başarısız gönderim.";
            }
            return log;
        }
        public string dosyaSunucuyaGonder(String fileName)
        {
            string log = "";

            using (HttpClient client = new HttpClient())
            {
                //"http://10.6.240.153:8081/cozumhbys/GetPatientQuery?requestType=pdksoku"
                log = "veriler sisoft sunucuya gonderiliyor..";
                string urlEk = httpBaglantilari.sisoftWebAdress + "requestType=pdksoku";

                client.DefaultRequestHeaders.Accept.Add(new System.Net.Http.Headers.MediaTypeWithQualityHeaderValue("application/text"));

                //FileStream fs = File.OpenRead(fileName);
                using (FileStream fs = File.OpenRead(fileName))
                {
                    var content = new StreamContent(fs);
                    var response = client.PostAsync(urlEk, content).Result;
                    if (response.StatusCode == System.Net.HttpStatusCode.OK)
                    {
                        string responseString = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
                        log += "\r\n" + " gonderim tamamlandi. islem başarılı, dönüş mesajı:" + responseString;
                    }
                    else
                        log += "\r\n" + " gonderim tamamlandi. başarısız gönderim.";
                    fs.Close();
                }
            }
            return log;
        }


        public userTags[] kullanicilariOku(string ip)
        {
            userTags[] users1 = null;

            //10.6.240.153:8081/cozumhbys/GetPatientQuery?requestType=pdksread&cardid=5444&ipadr=10.6.240.153

            string urlEk = httpBaglantilari.sisoftWebAdress + "requestType=pdkskart&rdkey=0&ipadres=" + ip.Trim();

            addLog("kullanicilariOku:baglanti:okunuyor:" + urlEk);

            var request = (HttpWebRequest)WebRequest.Create(urlEk); 

            var response = (HttpWebResponse)request.GetResponse();
            string responseString;
            
            using (var stream = response.GetResponseStream())
            {
                using (var reader = new StreamReader(stream))
                {
                    responseString = reader.ReadToEnd();
                    
                    if ((responseString != null) && responseString.Equals(""))
                    {
                        addLog("Sorgu sonucu personel kaydı bulunamadı");
                        return null;
                    } 

                    String[] results = responseString.Split(new[] { "\n", "\r\n" }, StringSplitOptions.None);
                    users1 = new userTags[results.Length];

                    addLog("kullanicilariOku:toplam " + results.Length + " personel kaydı bulundu");

                    for (int i= 0; i < results.Length; i++) {
                        String usSat = results[i];
                        
                        //Console.WriteLine(usSat);

                        String[] usSatAry = usSat.Split(new[] {"^"}, StringSplitOptions.None);
                        //String kart = idwEnrollNumber + "^" + idSname + "^" + sPassword + "^" + isPrivilege + "^" + sCardnumber + "^" + isEnabled + "^";

                        if (usSatAry.Length > 1)
                        {
                            users1[i] = new userTags();
                            users1[i].id = usSatAry[0];
                            users1[i].username = usSatAry[1];
                            users1[i].kartid = usSatAry[4];
                        }
                    }
                }
            }

          return users1;
        }
        private void addLog(string log)
        {
            if (this.cihazIslem != null)
                this.cihazIslem.addLog(log);
        }

    }


    public class userTags
    {
        public string username = "";
        public string id = "";
        public string kartid = "";

    }
}
