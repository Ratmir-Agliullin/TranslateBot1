import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Аглиуллины on 27.08.2017.
 */
public class VKManager {

    public static void getInfo() throws IOException {
        URL url = new URL("https://vk.com/milhistory");
        try {
            LineNumberReader reader = new LineNumberReader(new InputStreamReader(url.openStream()));
            String string = reader.readLine();
            while (string != null) {
                System.out.println(string);
                string = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        getInfo();
    }
}
