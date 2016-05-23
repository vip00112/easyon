package easyon.config;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/** 각종 설정 **/
public class Config {
    private static final Logger _log = Logger.getLogger(Config.class.getName());

    // config 파일 경로
    public static final String SERVER_CONFIG_PATH = "config/server.properties";
    public static final String DB_CONFIG_PATH = "config/db.properties";

    //**** server.properties ****//
    public static int PORT; // 소켓 서버 포트
    public static boolean WRITE_SOCKET_LOG; // 소켓 접근 기록 여부

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

            // 소켓 서버 포트
            PORT = Integer.parseInt(serverSettings.getProperty("Port", "2000"));

            // 소켓 접근 기록 여부
            WRITE_SOCKET_LOG = Boolean.parseBoolean(serverSettings.getProperty("WriteSocketLog", "false"));
        } catch (Exception e) {
            _log.log(Level.WARNING, "", e);
        }

        // db.properties
        try {
            Properties dbSettings = new Properties();
            dbSettings.load(new FileInputStream(DB_CONFIG_PATH));

            // DB 설정
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
