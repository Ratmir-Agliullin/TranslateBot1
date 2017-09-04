import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by Аглиуллины on 20.08.2017.
 */
public class DBManager {
public static String DBname;
    public static Connection conn;
    public static Statement statmt;
 public static ResultSet resSet;
  public static PreparedStatement prepSt;
    private static String name;

    public static void Conn() throws ClassNotFoundException, SQLException
    {
        conn = null;
        Class.forName("org.sqlite.JDBC");

        conn = DriverManager.getConnection("jdbc:sqlite:TEST1.sdb");

        System.out.println("База Подключена!");
    }

    public static void CloseDB() throws ClassNotFoundException, SQLException
    {
        conn.close();
//     prepSt.close();

        System.out.println("Соединения закрыты");
    }

    public static void CreateTable() throws SQLException, ClassNotFoundException {
        Conn();
        prepSt = conn.prepareStatement(" CREATE TABLE if not exists "+DBname+  " ('english' VARCHAR(255) PRIMARY KEY, 'russian' VARCHAR(255));");
       // prepSt.setString(1,DBname);

        prepSt.executeUpdate();
CloseDB();
    }



    public static void newEntity(String eng, String rus) throws SQLException, ClassNotFoundException {
Conn();
        prepSt = conn.prepareStatement(" INSERT or REPLACE INTO " + DBname+" (english, russian) VALUES (?, ?);");

        prepSt.setString(1,eng.toLowerCase());
        prepSt.setString(2,rus.toLowerCase());
        prepSt.executeUpdate();
        CloseDB();
    }

    public static String getEng(String rus) throws SQLException, ClassNotFoundException {
        Conn();
        String res = null;
         prepSt = conn.prepareStatement("SELECT english FROM " + DBname+"  WHERE russian=?");
        prepSt.setString(1,rus.toLowerCase());

        resSet =prepSt.executeQuery();
        res = resSet.getString("english");
        CloseDB();
        return res;
    }

    public static List<String> getEngList(String rus) throws SQLException, ClassNotFoundException {
        Conn();
        String res = null;
        List<String> stringList = new ArrayList<>();
        prepSt = conn.prepareStatement("SELECT english FROM " + DBname+"  WHERE russian=?");
        prepSt.setString(1,rus.toLowerCase());

        resSet =prepSt.executeQuery();
        while (resSet.next()){
            stringList.add(resSet.getString("english"));
        }
        CloseDB();
        return stringList;

    }


    public static List<String> getRusList(String eng) throws SQLException, ClassNotFoundException {
        Conn();
        String res = null;
        List<String> stringList = new ArrayList<>();
        prepSt = conn.prepareStatement("SELECT russian FROM " + DBname+"  WHERE english=?");
        prepSt.setString(1,eng.toLowerCase());
        resSet =prepSt.executeQuery();
        while (resSet.next()){
            stringList.add(resSet.getString("russian"));
        }
        CloseDB();
        return stringList;
    }


    public static String getRus(String eng) throws SQLException, ClassNotFoundException {
        Conn();
        String res = null;
         prepSt = conn.prepareStatement("SELECT russian FROM " + DBname+"  WHERE english=?");
        prepSt.setString(1,eng.toLowerCase());
          resSet =prepSt.executeQuery();
        res = resSet.getString("russian");
        CloseDB();
        return res;
    }

    public static void main(String[] args) {
        conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:TEST2.sdb");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        System.out.println("База Подключена!");
    }

}
