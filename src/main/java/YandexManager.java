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
    public static void getTranslate(String word, String lang) throws IOException {
String request = "https://translate.yandex.net/api/v1.5/tr.json/translate?" + "key="+API_KEY;
//        HttpClient httpclient = new DefaultHttpClient();
//        HttpPost httpPost = new HttpPost("https://translate.yandex.net");
//        StringEntity requestEntity = new StringEntity(request);
//        requestEntity.setContentType("application/x-www-form-urlencoded");
//         httpPost.setEntity(requestEntity);
//        HttpResponse response = httpclient.execute(httpPost);
        URL urlObj = new URL(request);
        HttpsURLConnection connection = (HttpsURLConnection)urlObj.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
        dataOutputStream.writeBytes("text=" + URLEncoder.encode(word, "UTF-8") + "&lang="+lang);
        InputStream response = connection.getInputStream();
        String json = new java.util.Scanner(response).nextLine();
       //Pattern p = Pattern.compile("(?<=\"text\"\:\\[\").+(?=\"\\])");
//       Pattern p = Pattern.compile("\\[\"(\\w+\\.)");
//        Matcher m = p.matcher(json.toString());
        String res=null;
//        if(m.find())  res=m.group(1); else res = "Нет";

        String re1=".*?";	// Non-greedy match on filler
        String re2="(?:[a-z][a-z]+)";	// Uninteresting: word
        String re3=".*?";	// Non-greedy match on filler
        String re4="((?:[a-z][a-z]+))";	// Word 1

        Pattern p = Pattern.compile("^\"text\":[\"(.*)\"]$");
        Matcher m = p.matcher(json.toString());
        if (m.find())
        {
res = m.group(0);
        } else res="нет";
        System.out.println(res.toString());
    }

    public static void main(String[] args) {
        try {
            getTranslate("как","ru-en");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
