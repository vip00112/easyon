package easyon.object.model;

import java.sql.Timestamp;
import java.util.List;

public class Chat {
    private int _no;
    private Timestamp _regdate;

    private List<User> _memberList; // 채팅 참가 유저 목록

    public Chat(int no, Timestamp regdate) {
        _no = no;
        _regdate = regdate;
    }

    /** 채팅창 번호 **/
    public int getNo() {
        return _no;
    }

    /** 채팅창 생성일 **/
    public Timestamp getRegdate() {
        return _regdate;
    }

    /** 채팅 참가 유저 목록 **/
    public List<User> getMemberList() {
        return _memberList;
    }

    /** 채팅 참가 유저 목록 설정 **/
    public void setMemberList(List<User> memberList) {
        _memberList = memberList;
    }

}
