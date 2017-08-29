import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import javax.net.ssl.HttpsURLConnection;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Аглиуллины on 27.08.2017.
 */
public class YandexManager {
public static String API_KEY = "trnsl.1.1.20170827T120830Z.509cdbe86298db83.370952a12cb668d63166f39baa76554946f3d23c";
    public static String getTranslate(String word, String lang) throws IOException {
String request = "https://translate.yandex.net/api/v1.5/tr.json/translate?" + "key="+API_KEY;

        URL urlObj = new URL(request);
        HttpsURLConnection connection = (HttpsURLConnection)urlObj.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
        dataOutputStream.writeBytes("text=" + URLEncoder.encode(word, "UTF-8") + "&lang="+lang);
        InputStream response = connection.getInputStream();
        String json = new java.util.Scanner(response).nextLine();

        String res=null;


        Pattern p = Pattern.compile("\\[\"(.*?)\\\"]");
        Matcher m = p.matcher(json.toString());
        if (m.find())
        {
res = m.group(0).substring(2,m.group(0).length()-2);
        } else res="нет";

        return res+"\n"+"Переведено сервисом «Яндекс.Переводчик»"+"http://translate.yandex.ru/";
    }

    public static void main(String[] args) {
        try {
            getTranslate("occasion","en-ru");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
