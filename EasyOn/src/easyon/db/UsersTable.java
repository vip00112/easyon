package easyon.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import easyon.object.ObjectManager;
import easyon.object.model.User;
import easyon.util.DBFactory;
import easyon.util.DBUtil;

/** 유저 목록 Table **/
public class UsersTable {
    private static final Logger _log = Logger.getLogger(UsersTable.class.getName());

    private static final UsersTable instance = new UsersTable();

    public static UsersTable getInstance() {
        return instance;
    }

    /** 모든 유저 정보 취득 **/
    public void getAllUsers() {
        System.out.println("++ Load users Table");
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            con = DBFactory.getInstance().getConnection();
            pstm = con.prepareStatement("SELECT no, id, password, nickname, statusMsg, profile FROM users");
            rs = pstm.executeQuery();
            while (rs.next()) {
                int no = rs.getInt("no");
                String id = rs.getString("id");
                String password = rs.getString("password");
                String nickname = rs.getString("nickname");
                String statusMsg = rs.getString("statusMsg");
                byte[] profile = rs.getBytes("profile");

                ObjectManager.getInstance().addUser(new User(no, id, password, nickname, statusMsg, profile));
            }
        } catch (Exception e) {
            _log.log(Level.WARNING, "", e);
        } finally {
            DBUtil.close(con, pstm, rs);
        }
    }

    /** 유저 정보 업데이트
     *  @param user 업데이트할 유저 **/
    public boolean updateUserInfo(User user) {
        Connection con = null;
        PreparedStatement pstm = null;

        try {
            con = DBFactory.getInstance().getConnection();
            pstm = con.prepareStatement("UPDATE users SET nickname=?, statusMsg=?, profile=? WHERE no=?");
            pstm.setString(1, user.getNickname());
            pstm.setString(2, user.getStatusMsg());
            pstm.setBytes(3, user.getProfile());
            pstm.setInt(4, user.getNo());
            return pstm.execute();
        } catch (Exception e) {
            _log.log(Level.WARNING, "", e);
        } finally {
            DBUtil.close(con, pstm);
        }
        return false;
    }

}
