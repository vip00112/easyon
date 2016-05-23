package easyon.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/** Database ���� �� Connection pool <br>
 *  Connectoin pool�� ���� c3p0 ���̺귯�� ��� **/
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

    /** DB �������� ����
     *  @param drive ����̹�
     *  @param url url
     *  @param user ������
     *  @param password ��й�ȣ **/
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

            // ���� �׽�Ʈ
            _source.getConnection().close();
        } catch (Exception e) {
            _log.log(Level.WARNING, "", e);
        }

        System.out.println("++ Setting Database");
    }

    /** DB �������� �ı�**/
    public void destroy() {
        _source.close();
        _source = null;
    }

    /** pool���� Connection ��ü ���
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