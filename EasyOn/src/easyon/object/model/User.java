package easyon.object.model;

import java.util.List;

import easyon.db.UsersBuddyTable;
import easyon.db.UsersTable;
import easyon.object.ObjectManager;
import easyon.server.EasyClient;
import easyon.server.packet.serverPacket.S_Buddy;
import easyon.server.packet.serverPacket.WritePacket;

/** ���� ���� ���� **/
public class User {
    private int _no;
    private String _id;
    private String _password;
    private String _nickname;
    private String _statusMsg;
    private byte[] _profile;

    private boolean _isOnline;
    private String _groupName; // �Ҽӵ� �׷�
    private List<User> _buddyList; // ģ�� ���
    private List<String> _groupList; // ���� ���� �׷�
    private EasyClient _client;

    public User(int no, String id, String password, String nickname, String statusMsg, byte[] profile) {
        _no = no;
        _id = id;
        _password = password;
        _nickname = nickname;
        _statusMsg = statusMsg;
        _profile = profile;
    }

    /** ������ ������Ʈ **/
    public void update(String nickname, String statusMsg, byte[] profile) {
        _nickname = nickname;
        _statusMsg = statusMsg;
        _profile = profile;

        UsersTable.getInstance().updateUserInfo(this);

    }

    /** ���� ��ȣ **/
    public int getNo() {
        return _no;
    }

    /** ���� **/
    public String getId() {
        return _id;
    }

    /** ��й�ȣ **/
    public String getPassword() {
        return _password;
    }

    /** �г��� **/
    public String getNickname() {
        return _nickname;
    }

    /** ���� �޽��� **/
    public String getStatusMsg() {
        return _statusMsg;
    }

    /** ������ ����(���� ���̳ʸ�) **/
    public byte[] getProfile() {
        return _profile;
    }

    /** �¶��� ���� **/
    public boolean isOnline() {
        return _isOnline;
    }

    /** �¶��� ���� ���� **/
    public void setOnline(boolean flag) {
        _isOnline = flag;
    }

    /** ģ�� �׷� **/
    public String getGroupName() {
        return _groupName;
    }

    /** �׷� ���� **/
    public void setGroupName(String groupName) {
        _groupName = groupName;
    }

    /** ģ�� ��� **/
    public List<User> getBuddyList() {
        return _buddyList;
    }

    /** ģ�� �߰�
     *  @param buddyNo �߰��� ģ�� ��ȣ
     *  @param groupName �׷��
     *  @param requestMsg ��û �޽��� **/
    public void addBuddy(int buddyNo, String groupName, String requestMsg) {
        User buddy = ObjectManager.getInstance().getUser(buddyNo);
        if (buddy != null) {
            _buddyList.add(buddy);
            UsersBuddyTable.getInstance().addBuddy(_no, buddyNo, groupName);
            UsersBuddyTable.getInstance().addBuddyRequest(buddyNo, _no, requestMsg);
        }
    }

    /** ģ�� �߰� : ģ�� ��û������ ����
     *  @param isYes True:����, False:����
     *  @param buddyNo �߰��� ģ�� ��ȣ
     *  @param groupName �׷�� **/
    public void addBuddy(boolean isYes, int buddyNo, String groupName) {
        User buddy = ObjectManager.getInstance().getUser(buddyNo);
        if (buddy != null) {
            if (isYes) {
                _buddyList.add(buddy);
                UsersBuddyTable.getInstance().addBuddy(_no, buddyNo, groupName);
            }
            UsersBuddyTable.getInstance().removeBuddyRequest(_no, buddyNo);
        }
    }

    /** ģ�� ����
     *  @param buddyNo ������ ģ�� ��ȣ **/
    public void removeBuddy(int buddyNo) {
        if (UsersBuddyTable.getInstance().removeBuddy(_no, buddyNo)) {
            User[] buddys = _buddyList.toArray(new User[_buddyList.size()]);
            for (User buddy : buddys) {
                if (buddy.getNo() == buddyNo) {
                    _buddyList.remove(buddy);
                    break;
                }
            }
        }
    }

    /** ģ�� �׷� ����
     *  @param buddyNo ������ ģ�� ��ȣ
     *  @param groupName ������ �׷�� **/
    public void updateGroupName(int buddyNo, String groupName) {
        if (UsersBuddyTable.getInstance().updateBuddy(_no, buddyNo, groupName)) {
            User[] buddys = _buddyList.toArray(new User[_buddyList.size()]);
            for (User buddy : buddys) {
                if (buddy.getNo() == buddyNo) {
                    buddy.setGroupName(groupName);
                    break;
                }
            }
        }
    }

    /** ���� ���� �׷� ��� **/
    public List<String> getGroupList() {
        return _groupList;
    }

    /** ���� ���� �׷� ���� **/
    public void setGroupList(List<String> groupList) {
        _groupList = groupList;
    }

    /** User�� Client ���� **/
    public void setClient(EasyClient client) {
        _client = client;
    }

    /** ��Ŷ ����
     *  @param packet ������ ��Ŷ ��ü **/
    public void sendPacket(WritePacket packet) {
        if (_isOnline && _client != null) {
            _client.sendPacket(packet);
        }
    }

    /** �α��� ó�� **/
    public void login(EasyClient client) {
        _client = client;
        _isOnline = true;
        _buddyList = UsersBuddyTable.getInstance().getBuddys(_no);
        _groupList = UsersBuddyTable.getInstance().getGroups(_no);

        // �� ������ ģ������ �˸�
        for (User buddy : _buddyList) {
            buddy.sendPacket(new S_Buddy(S_Buddy.TYPE_LOGIN, true, this));
        }
    }

    /** �α׾ƿ�  ó�� **/
    public void logout() {
        // �� �α׾ƿ��� ģ������ �˸�
        for (User buddy : _buddyList) {
            buddy.sendPacket(new S_Buddy(S_Buddy.TYPE_LOGIN, false, this));
        }

        _client = null;
        _isOnline = false;
        _buddyList = null;
        _groupList = null;
    }

}
