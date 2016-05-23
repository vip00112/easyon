package easyon.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import easyon.object.ObjectManager;
import easyon.object.model.Chat;
import easyon.object.model.ChatLog;
import easyon.object.model.User;
import easyon.util.DBFactory;
import easyon.util.DBUtil;

public class ChatTable {
    private static final Logger _log = Logger.getLogger(ChatTable.class.getName());

    private static final ChatTable instance = new ChatTable();

    public static ChatTable getInstance() {
        return instance;
    }

    /** 모든 채팅 정보 취득 **/
    public void getAllChats() {
        System.out.println("++ Load chat_list Table");
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            con = DBFactory.getInstance().getConnection();
            pstm = con.prepareStatement("SELECT no, regdate FROM chat_list");
            rs = pstm.executeQuery();
            while (rs.next()) {
                int no = rs.getInt("no");
                Timestamp regdate = rs.getTimestamp("regdate");

                Chat chat = new Chat(no, regdate);
                chat.setMemberList(getChatMemberList(no));

                ObjectManager.getInstance().addChat(chat);
            }
        } catch (Exception e) {
            _log.log(Level.WARNING, "", e);
        } finally {
            DBUtil.close(con, pstm, rs);
        }
    }

    // 해당 채팅의 참가 유저 목록
    private List<User> getChatMemberList(int no) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        List<User> memberList = new ArrayList<User>();

        try {
            con = DBFactory.getInstance().getConnection();
            pstm = con.prepareStatement("SELECT userNo FROM chat_users WHERE chatNo=?");
            pstm.setInt(1, no);
            rs = pstm.executeQuery();
            while (rs.next()) {
                int userNo = rs.getInt("userNo");
                User user = ObjectManager.getInstance().getUser(userNo);
                if (user != null) {
                    memberList.add(user);
                }
            }
        } catch (Exception e) {
            _log.log(Level.WARNING, "", e);
        } finally {
            DBUtil.close(con, pstm, rs);
        }
        return memberList;
    }

    /** 새로운 채팅방 개설
     *  @param memberList 채팅 참가 유저 목록 **/
    public Chat addChat(List<User> memberList) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Timestamp regdate = new Timestamp(System.currentTimeMillis());
        Chat chat = null;

        try {
            con = DBFactory.getInstance().getConnection();
            pstm = con.prepareStatement("INSERT INTO chat_list SET regdate=?");
            pstm.setTimestamp(1, regdate);
            pstm.execute();

            pstm.close();
            pstm = con.prepareStatement("SELECT LAST_INSERT_ID()");
            rs = pstm.executeQuery();
            if (rs.next()) {
                int no = rs.getInt(1);
                chat = new Chat(no, regdate);
                chat.setMemberList(memberList);
                ObjectManager.getInstance().addChat(chat);

                for (User member : memberList) {
                    addChatMember(no, member.getNo());
                }
            }
        } catch (Exception e) {
            _log.log(Level.WARNING, "", e);
        } finally {
            DBUtil.close(con, pstm, rs);
        }
        return chat;
    }

    // 새로운 채팅 참가 유저 추가
    private void addChatMember(int chatNo, int userNo) {
        Connection con = null;
        PreparedStatement pstm = null;

        try {
            con = DBFactory.getInstance().getConnection();
            pstm = con.prepareStatement("INSERT INTO chat_users SET chatNo=?, userNo=?");
            pstm.setInt(1, chatNo);
            pstm.setInt(2, userNo);
            pstm.execute();
        } catch (Exception e) {
            _log.log(Level.WARNING, "", e);
        } finally {
            DBUtil.close(con, pstm);
        }
    }

    /** 해당 유저의 채팅 목록 획득
     *  @param userNo 유저 번호 **/
    public List<Chat> getChats(int userNo) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        List<Chat> chatList = new ArrayList<Chat>();

        try {
            con = DBFactory.getInstance().getConnection();
            pstm = con.prepareStatement("SELECT chatNo FROM chat_users WHERE userNo=?");
            pstm.setInt(1, userNo);
            rs = pstm.executeQuery();
            while (rs.next()) {
                int no = rs.getInt("chatNo");
                Chat chat = ObjectManager.getInstance().getChat(no);
                if (chat != null) {
                    chatList.add(chat);
                }
            }
        } catch (Exception e) {
            _log.log(Level.WARNING, "", e);
        } finally {
            DBUtil.close(con, pstm, rs);
        }
        return chatList;
    }

    /** 해당 채팅의 로그 기록
     *  @param **/
    public void addChatLog(int chatNo, User user, String msg) {
        Connection con = null;
        PreparedStatement pstm = null;

        try {
            con = DBFactory.getInstance().getConnection();
            pstm = con.prepareStatement("INSERT INTO chat_log SET chatNo=?, userNo=?, nickname=?, msg=?, regdate=?");
            pstm.setInt(1, chatNo);
            pstm.setInt(2, user.getNo());
            pstm.setString(3, user.getNickname());
            pstm.setString(4, msg);
            pstm.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            pstm.execute();
        } catch (Exception e) {
            _log.log(Level.WARNING, "", e);
        } finally {
            DBUtil.close(con, pstm);
        }
    }

    /** 해당 채팅의 로그 목록 획득
     *  @param chatNo 채팅 번호 **/
    public List<ChatLog> getChatLogs(int chatNo) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        List<ChatLog> chatLogList = new ArrayList<ChatLog>();

        try {
            con = DBFactory.getInstance().getConnection();
            pstm = con.prepareStatement("SELECT no, userNo, nickname, msg, regdate FROM chat_log WHERE chatNo=? ORDER BY no ASC");
            pstm.setInt(1, chatNo);
            rs = pstm.executeQuery();
            while (rs.next()) {
                int no = rs.getInt("no");
                int userNo = rs.getInt("userNo");
                String nickname = rs.getString("nickname");
                String msg = rs.getString("msg");
                Timestamp regdate = rs.getTimestamp("regdate");
                chatLogList.add(new ChatLog(no, chatNo, userNo, nickname, msg, regdate));
            }
        } catch (Exception e) {
            _log.log(Level.WARNING, "", e);
        } finally {
            DBUtil.close(con, pstm, rs);
        }
        return chatLogList;
    }

}
