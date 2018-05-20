package tmall.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @program: tmall
 * @description:
 * @author: Mr.Longyy
 * @create: 2018-05-15 22:20
 **/
public class DBUtil {
    static String ip ="192.168.110.129";
    static int port = 3306;
    static String database = "tmall";
    static String encoding = "UTF-8";
    static String loginName = "mmall";
    static String password = "mmall";

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        String url = String.format("jdbc:mysql://%s:%d/%s?characterEncoding=%s",ip,port,database,encoding);
        return DriverManager.getConnection(url,loginName,password);
    }

}
