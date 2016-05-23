package easyon.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/** Database 정보 및 Connection pool <br>
 *  Connectoin pool을 위한 c3p0 라이브러리 사용 **/
public class DBFactory {
    private static final Logger _log = Logger.getLogger(DBFactory.class.getName());

    private static final DBFactory instance = new DBFactory();

    public static DBFactory getInstance() {
        return instance;
    }

    private ComboPooledDataSource _source;
    private String _driver;
    private String _url;
    private String _user;
    private String _password;

    /** DB 접속정보 설정
     *  @param drive 드라이버
     *  @param url url
     *  @param user 유저명
     *  @param password 비밀번호 **/
    public void setting(String driver, String url, String user, String password) {
        _driver = driver;
        _url = url;
        _user = user;
        _password = password;

        try {
            _source = new ComboPooledDataSource();
            _source.setDriverClass(_driver);
            _source.setJdbcUrl(_url);
            _source.setUser(_user);
            _source.setPassword(_password);

            _source.setMinPoolSize(3);
            _source.setMaxPoolSize(20);
            _source.setMaxStatements(500);
            _source.setAcquireIncrement(1);

            // 연결 테스트
            _source.getConnection().close();
        } catch (Exception e) {
            _log.log(Level.WARNING, "", e);
        }

        System.out.println("++ Setting Database");
    }

    /** DB 접속정보 파기**/
    public void destroy() {
        _source.close();
        _source = null;
    }

    /** pool에서 Connection 객체 취득
     *  @return java.sql.Connection **/
    public Connection getConnection() {
        Connection con = null;
        while (con == null) {
            try {
                con = _source.getConnection();
            } catch (SQLException e) {
                System.out.println("Database getConnection() failed, try again\r\n" + e);
            } catch (Exception e) {
                _log.log(Level.WARNING, "", e);
            }
        }
        return con;
    }

}