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
import easyon.object.model.Memo;
import easyon.object.model.User;
import easyon.util.DBFactory;
import easyon.util.DBUtil;

public class MemoTable {
    private static final Logger _log = Logger.getLogger(MemoTable.class.getName());

    private static final MemoTable instance = new MemoTable();

    public static MemoTable getInstance() {
        return instance;
    }

    /** 쪽지 작성
     *  @param memo 쪽지 정보 **/
    public void writeMemo(Memo memo) {
        Connection con = null;
        PreparedStatement pstm = null;

        try {
            con = DBFactory.getInstance().getConnection();
            pstm = con.prepareStatement("INSERT INTO memos SET writerNo=?, readerNo=?, msg=?, isRead=?, regdate=?");
            pstm.setInt(1, memo.getWriterNo());
            pstm.setInt(2, memo.getReaderNo());
            pstm.setString(3, memo.getMsg());
            pstm.setInt(4, memo.isRead() ? 1 : 0);
            pstm.setTimestamp(5, memo.getRegdate());
            pstm.execute();
        } catch (Exception e) {
            _log.log(Level.WARNING, "", e);
        } finally {
            DBUtil.close(con, pstm);
        }
    }

    /** 해당 유저의 미확인 수신 쪽지 갯수
     *  @param userNo 유저 번호 **/
    public int getNotReadCount(int userNo) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        int count = 0;

        try {
            con = DBFactory.getInstance().getConnection();
            pstm = con.prepareStatement("SELECT count(*) cnt FROM memos WHERE readerNo=? AND isRead=0");
            pstm.setInt(1, userNo);
            rs = pstm.executeQuery();
            if (rs.next()) {
                count = rs.getInt("cnt");
            }
        } catch (Exception e) {
            _log.log(Level.WARNING, "", e);
        } finally {
            DBUtil.close(con, pstm, rs);
        }
        return count;

    }

    /** 해당 유저의 수신 쪽지 목록 획득
     *  @param writerNo 보낸 유저 번호
     *  @param readerNo 받은 유저 번호 **/
    public List<Memo> getMemoList(int writerNo, int readerNo) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        List<Memo> memoList = new ArrayList<Memo>();

        try {
            con = DBFactory.getInstance().getConnection();
            if (writerNo > 0) {
                pstm = con.prepareStatement("SELECT no, writerNo, readerNo, msg, isRead, regdate FROM memos WHERE writerNo=? AND readerNo=? ORDER BY regdate DESC");
                pstm.setInt(1, writerNo);
                pstm.setInt(2, readerNo);
            } else {
                pstm = con.prepareStatement("SELECT no, writerNo, readerNo, msg, isRead, regdate FROM memos WHERE readerNo=? ORDER BY regdate DESC");
                pstm.setInt(1, readerNo);
            }
            rs = pstm.executeQuery();

            while (rs.next()) {
                int no = rs.getInt("no");
                writerNo = rs.getInt("writerNo");
                readerNo = rs.getInt("readerNo");
                String msg = rs.getString("msg");
                boolean isRead = rs.getInt("isRead") == 1 ? true : false;
                Timestamp regdate = rs.getTimestamp("regdate");

                Memo memo = new Memo(no, writerNo, readerNo, msg, isRead, regdate);
                User writer = ObjectManager.getInstance().getUser(writerNo);
                if (writer != null) {
                    memo.setWriter(writer.getId(), writer.getNickname());
                }
                memoList.add(memo);
            }
        } catch (Exception e) {
            _log.log(Level.WARNING, "", e);
        } finally {
            DBUtil.close(con, pstm, rs);
        }
        return memoList;
    }

    /** 해당 쪽지 읽음 처리
     *  @param no 쪽지 번호 **/
    public void readMemo(int no) {
        Connection con = null;
        PreparedStatement pstm = null;

        try {
            con = DBFactory.getInstance().getConnection();
            pstm = con.prepareStatement("UPDATE memos SET isRead=1 WHERE no=?");
            pstm.setInt(1, no);
            pstm.execute();
        } catch (Exception e) {
            _log.log(Level.WARNING, "", e);
        } finally {
            DBUtil.close(con, pstm);
        }
    }
}
