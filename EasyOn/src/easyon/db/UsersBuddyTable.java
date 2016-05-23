package easyon.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import easyon.object.ObjectManager;
import easyon.object.model.User;
import easyon.util.DBFactory;
import easyon.util.DBUtil;

/** 유저 친구 목록 Table **/
public class UsersBuddyTable {
    private static final Logger _log = Logger.getLogger(UsersBuddyTable.class.getName());

    private static final UsersBuddyTable instance = new UsersBuddyTable();

    public static UsersBuddyTable getInstance() {
        return instance;
    }

    /** 해당 유저의 친구 목록 획득
     *  @param userNo 유저 번호 **/
    public List<User> getBuddys(int userNo) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        List<User> buddys = new ArrayList<User>();

        try {
            con = DBFactory.getInstance().getConnection();
            pstm = con.prepareStatement("SELECT buddyNo, groupName FROM users_buddy WHERE userNo=?");
            pstm.setInt(1, userNo);
            rs = pstm.executeQuery();
            while (rs.next()) {
                int buddyNo = rs.getInt("buddyNo");
                String group = rs.getString("groupName");
                User user = ObjectManager.getInstance().getUser(buddyNo);
                if (user != null) {
                    user.setGroupName(group);
                    buddys.add(user);
                }
            }
        } catch (Exception e) {
            _log.log(Level.WARNING, "", e);
        } finally {
            DBUtil.close(con, pstm, rs);
        }
        return buddys;
    }

    /** 해당 유저의 친구 추가 목록 획득
     *  @param user 유저 번호 **/
    public Map<User, String> getBuddyRequests(User user) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Map<User, String> resultMap = new HashMap<User, String>();

        try {
            con = DBFactory.getInstance().getConnection();
            pstm = con.prepareStatement("SELECT buddyNo, requestMsg FROM users_buddy_request WHERE userNo=?");
            pstm.setInt(1, user.getNo());
            rs = pstm.executeQuery();
            while (rs.next()) {
                int buddyNo = rs.getInt("buddyNo");
                String requestMsg = rs.getString("requestMsg");
                User buddy = ObjectManager.getInstance().getUser(buddyNo);
                if (buddy != null && !user.getBuddyList().contains(buddy)) {
                    resultMap.put(buddy, requestMsg);
                }
            }
        } catch (Exception e) {
            _log.log(Level.WARNING, "", e);
        } finally {
            DBUtil.close(con, pstm, rs);
        }
        return resultMap;
    }

    /** 해당 유저의 그룹 목록 획득
     *  @param userNo 유저 번호 **/
    public List<String> getGroups(int userNo) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        List<String> groupList = new ArrayList<String>();
        groupList.add("기타");

        try {
            con = DBFactory.getInstance().getConnection();
            pstm = con.prepareStatement("SELECT groupNames FROM users_buddy_group WHERE userNo=?");
            pstm.setInt(1, userNo);
            rs = pstm.executeQuery();
            while (rs.next()) {
                StringTokenizer tokens = new StringTokenizer(rs.getString("groupNames"), ",");
                while (tokens.hasMoreElements()) {
                    String groupName = tokens.nextToken();
                    if (!groupList.contains(groupName)) {
                        groupList.add(groupName);
                    }
                }
            }
        } catch (Exception e) {
            _log.log(Level.WARNING, "", e);
        } finally {
            DBUtil.close(con, pstm, rs);
        }
        return groupList;
    }

    /** 친구 추가
     *  @param userNo 유저 번호
     *  @param buddyNo 추가할 친구의 유저 번호
     *  @param groupName 그룹명
     *  @return 성공:true, 실패:false **/
    public boolean addBuddy(int userNo, int buddyNo, String groupName) {
        Connection con = null;
        PreparedStatement pstm = null;

        try {
            con = DBFactory.getInstance().getConnection();
            pstm = con.prepareStatement("INSERT INTO users_buddy SET userNo=?, buddyNo=?, groupName=?");
            pstm.setInt(1, userNo);
            pstm.setInt(2, buddyNo);
            pstm.setString(3, groupName);
            return pstm.execute();
        } catch (Exception e) {
            _log.log(Level.WARNING, "", e);
        } finally {
            DBUtil.close(con, pstm);
        }
        return false;
    }

    /** 친구 추가 요청
     *  @param userNo 신청 받은 유저 번호
     *  @param buddyNo 신청한 유저 번호
     *  @param requestMsg 신청 메시지
     *  @return 성공:true, 실패:false **/
    public boolean addBuddyRequest(int userNo, int buddyNo, String requestMsg) {
        Connection con = null;
        PreparedStatement pstm = null;

        try {
            con = DBFactory.getInstance().getConnection();
            pstm = con.prepareStatement("INSERT INTO users_buddy_request SET userNo=?, buddyNo=?, requestMsg=?");
            pstm.setInt(1, userNo);
            pstm.setInt(2, buddyNo);
            pstm.setString(3, requestMsg);
            return pstm.execute();
        } catch (Exception e) {
            _log.log(Level.WARNING, "", e);
        } finally {
            DBUtil.close(con, pstm);
        }
        return false;
    }

    /** 친구 삭제
     *  @param userNo 유저 번호
     *  @param buddyNo 삭제할 친구의 유저 번호
     *  @return 성공:true, 실패:false **/
    public boolean removeBuddy(int userNo, int buddyNo) {
        Connection con = null;
        PreparedStatement pstm = null;

        try {
            con = DBFactory.getInstance().getConnection();
            pstm = con.prepareStatement("DELETE FROM users_buddy WHERE userNo=? AND buddyNo=?");
            pstm.setInt(1, userNo);
            pstm.setInt(2, buddyNo);
            return pstm.execute();
        } catch (Exception e) {
            _log.log(Level.WARNING, "", e);
        } finally {
            DBUtil.close(con, pstm);
        }
        return false;
    }

    /** 친구 추가 요청 삭제
     *  @param userNo 신청 받은 유저 번호
     *  @param buddyNo 신청한 유저 번호
     *  @return 성공:true, 실패:false **/
    public boolean removeBuddyRequest(int userNo, int buddyNo) {
        Connection con = null;
        PreparedStatement pstm = null;

        try {
            con = DBFactory.getInstance().getConnection();
            pstm = con.prepareStatement("DELETE FROM users_buddy_request WHERE userNo=? AND buddyNo=?");
            pstm.setInt(1, userNo);
            pstm.setInt(2, buddyNo);
            return pstm.execute();
        } catch (Exception e) {
            _log.log(Level.WARNING, "", e);
        } finally {
            DBUtil.close(con, pstm);
        }
        return false;
    }

    /** 친구 정보 변경
     *  @param userNo 유저 번호
     *  @param buddyNo 변경할 유저 번호
     *  @param groupName 변경할 그룹명 **/
    public boolean updateBuddy(int userNo, int buddyNo, String groupName) {
        Connection con = null;
        PreparedStatement pstm = null;

        try {
            con = DBFactory.getInstance().getConnection();
            pstm = con.prepareStatement("UPDATE users_buddy SET groupName=? WHERE userNo=? AND buddyNo=?");
            pstm.setString(1, groupName);
            pstm.setInt(2, userNo);
            pstm.setInt(3, buddyNo);
            return pstm.execute();
        } catch (Exception e) {
            _log.log(Level.WARNING, "", e);
        } finally {
            DBUtil.close(con, pstm);
        }
        return false;
    }

    /** 유저의 그룹 정보 변경
     *  @param userNo 유저 번호
     *  @param groupList 그룹명 **/
    public boolean updateGroups(int userNo, List<String> groupList) {
        Connection con = null;
        PreparedStatement pstm = null;
        String groupNames = "";
        for (String group : groupList) {
            groupNames += group + ",";
        }

        try {
            con = DBFactory.getInstance().getConnection();
            if (getGroups(userNo).size() > 1) {
                pstm = con.prepareStatement("UPDATE users_buddy_group SET groupNames=? WHERE userNo=?");
                pstm.setString(1, groupNames);
                pstm.setInt(2, userNo);
            } else {
                pstm = con.prepareStatement("INSERT INTO users_buddy_group userNo=?, groupNames=?");
                pstm.setInt(1, userNo);
                pstm.setString(2, groupNames);
            }
            return pstm.execute();
        } catch (Exception e) {
            _log.log(Level.WARNING, "", e);
        } finally {
            DBUtil.close(con, pstm);
        }
        return false;
    }

}
