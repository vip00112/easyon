package easyon.object.model;

import java.sql.Timestamp;

/** 쪽지 정보 **/
public class Memo {
    private int _no;
    private int _writerNo;
    private int _readerNo;
    private String _msg;
    private boolean _isRead;
    private Timestamp _regdate;
    
    private String _writerId;
    private String _writerNickname;

    public Memo(int no, int writerNo, int readerNo, String msg, boolean isRead, Timestamp regdate) {
        _no = no;
        _writerNo = writerNo;
        _readerNo = readerNo;
        _msg = msg;
        _isRead = isRead;
        _regdate = regdate;
    }

    public Memo(int writerNo, int readerNo, String msg, boolean isRead, Timestamp regdate) {
        _writerNo = writerNo;
        _readerNo = readerNo;
        _msg = msg;
        _isRead = isRead;
        _regdate = regdate;
    }

    public int getNo() {
        return _no;
    }

    public int getWriterNo() {
        return _writerNo;
    }

    public int getReaderNo() {
        return _readerNo;
    }

    public String getMsg() {
        return _msg;
    }

    public boolean isRead() {
        return _isRead;
    }
    
    public void setRead(boolean flag) {
        _isRead = flag;
    }

    public Timestamp getRegdate() {
        return _regdate;
    }

    public String getWriterId() {
        return _writerId;
    }

    public String getWriterNickname() {
        return _writerNickname;
    }
    
    public void setWriter(String id, String nickname) {
        _writerId = id;
        _writerNickname = nickname;
    }

}
