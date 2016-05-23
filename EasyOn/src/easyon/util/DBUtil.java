package easyon.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBUtil {
    private static final Logger _log = Logger.getLogger(DBUtil.class.getName());

    /** ���ҽ� ����
     *  @param con Connection **/
    public static void close(Connection con) {
        try {
            if (con != null) {
                con.close();
            }
        } catch (Exception e) {
            _log.log(Level.WARNING, "", e);
        }
    }

    /** ���ҽ� ����
     *  @param stm Statement  **/
    public static void close(Statement stm) {
        try {
            if (stm != null) {
                stm.close();
            }
        } catch (Exception e) {
            _log.log(Level.WARNING, "", e);
        }
    }

    /** ���ҽ� ����
     *  @param rs ResultSet **/
    public static void close(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (Exception e) {
            _log.log(Level.WARNING, "", e);
        }
    }

    /** ���ҽ� ����
     *  @param con Connection
     *  @param stm Statement **/
    public static void close(Connection con, Statement stm) {
        close(stm);
        close(con);
    }

    /** ���ҽ� ����
     *  @param con Connection
     *  @param stm Statement
     *  @param rs ResultSet **/
    public static void close(Connection con, Statement stm, ResultSet rs) {
        close(rs);
        close(stm);
        close(con);
    }

}
