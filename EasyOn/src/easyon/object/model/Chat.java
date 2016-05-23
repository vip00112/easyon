package easyon.object.model;

import java.sql.Timestamp;
import java.util.List;

public class Chat {
    private int _no;
    private Timestamp _regdate;

    private List<User> _memberList; // ä�� ���� ���� ���

    public Chat(int no, Timestamp regdate) {
        _no = no;
        _regdate = regdate;
    }

    /** ä��â ��ȣ **/
    public int getNo() {
        return _no;
    }

    /** ä��â ������ **/
    public Timestamp getRegdate() {
        return _regdate;
    }

    /** ä�� ���� ���� ��� **/
    public List<User> getMemberList() {
        return _memberList;
    }

    /** ä�� ���� ���� ��� ���� **/
    public void setMemberList(List<User> memberList) {
        _memberList = memberList;
    }

}
