package easyon.object.model;

import java.sql.Timestamp;

public class ChatLog {
    private int _no;
    private int _chatNo;
    private int _userNo;
    private String _nickname;
    private String _msg;
    private Timestamp _regdate;

    public ChatLog(int no, int chatNo, int userNo, String nickname, String msg, Timestamp regdate) {
        _no = no;
        _chatNo = chatNo;
        _userNo = userNo;
        _nickname = nickname;
        _msg = msg;
        _regdate = regdate;
    }

    public int getNo() {
        return _no;
    }

    public int getChatNo() {
        return _chatNo;
    }

    public int getUserNo() {
        return _userNo;
    }
    
    public String getNickname() {
        return _nickname;
    }

    public String getMsg() {
        return _msg;
    }

    public Timestamp getRegdate() {
        return _regdate;
    }
}
