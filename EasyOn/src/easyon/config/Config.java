package easyon.config;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/** ���� ���� **/
public class Config {
    private static final Logger _log = Logger.getLogger(Config.class.getName());

    // config ���� ���
    public static final String SERVER_CONFIG_PATH = "config/server.properties";
    public static final String DB_CONFIG_PATH = "config/db.properties";

    //**** server.properties ****//
    public static int PORT; // ���� ���� ��Ʈ
    public static boolean WRITE_SOCKET_LOG; // ���� ���� ��� ����

    //**** db.properties ****//
    public static String DRIVER;
    public static String URL;
    public static String USER;
    public static String PASSWORD;

    public static void load() {
        // server.properties
        try {
            Properties serverSettings = new Properties();
            serverSettings.load(new FileInputStream(SERVER_CONFIG_PATH));

            // ���� ���� ��Ʈ
            PORT = Integer.parseInt(serverSettings.getProperty("Port", "2000"));

            // ���� ���� ��� ����
            WRITE_SOCKET_LOG = Boolean.parseBoolean(serverSettings.getProperty("WriteSocketLog", "false"));
        } catch (Exception e) {
            _log.log(Level.WARNING, "", e);
        }

        // db.properties
        try {
            Properties dbSettings = new Properties();
            dbSettings.load(new FileInputStream(DB_CONFIG_PATH));

            // DB ����
            DRIVER = dbSettings.getProperty("Driver", "com.mysql.jdbc.Driver");
            URL = dbSettings.getProperty("URL", "jdbc:mysql://localhost/easyon?useUnicode=true&characterEncoding=utf8&autoReconnect=true");
            USER = dbSettings.getProperty("User", "root");
            PASSWORD = dbSettings.getProperty("Password", "");
        } catch (Exception e) {
            _log.log(Level.WARNING, "", e);
        }

        System.out.println("++ Load Config Files");
    }
}
