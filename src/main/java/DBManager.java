import java.sql.*;

/**
 * Created by Аглиуллины on 20.08.2017.
 */
public class DBManager {

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
        statmt.close();
        resSet.close();

        System.out.println("Соединения закрыты");
    }

    public static void newEntity(String eng, String rus) throws SQLException, ClassNotFoundException {
Conn();
        prepSt = conn.prepareStatement("INSERT INTO English (english, russian) VALUES (?, ?);");
        prepSt.setString(1,eng.toLowerCase());
        prepSt.setString(2,rus.toLowerCase());
        prepSt.executeUpdate();

    }

    public static String getEng(String rus) throws SQLException, ClassNotFoundException {
        Conn();
        String res = null;
        prepSt = conn.prepareStatement("SELECT english FROM English WHERE russian=?");
        prepSt.setString(1,rus);
        resSet =prepSt.executeQuery();
        res = resSet.getString("english");
        return res;
    }

    public static String getRus(String eng) throws SQLException, ClassNotFoundException {
        Conn();
        String res = null;
        prepSt = conn.prepareStatement("SELECT russian FROM English WHERE english=?");
        prepSt.setString(1,eng);
        resSet =prepSt.executeQuery();
        res = resSet.getString("russian");
        return res;
    }

    public static void addToBuffer(String word) throws SQLException, ClassNotFoundException {

        prepSt = conn.prepareStatement("INSERT into table_name(word) VALUES word=?");
        prepSt.setString(1,word);
        resSet =prepSt.executeQuery();
    }

    public static String getWord() throws SQLException {
        String res = null;
        prepSt = conn.prepareStatement("SELECT word FROM table_name WHERE id=1");
           resSet =prepSt.executeQuery();
        res = resSet.getString("word");
        return res;
    }

    public static void deleteWord() throws SQLException {
        prepSt = conn.prepareStatement("DELETE word FROM table_name WHERE id=1");
        resSet =prepSt.executeQuery();
    }

}
