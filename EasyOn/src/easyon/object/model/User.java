package easyon.object.model;

import java.util.List;

import easyon.db.UsersBuddyTable;
import easyon.db.UsersTable;
import easyon.object.ObjectManager;
import easyon.server.EasyClient;
import easyon.server.packet.serverPacket.S_Buddy;
import easyon.server.packet.serverPacket.WritePacket;

/** 유저 정보 관리 **/
public class User {
    private int _no;
    private String _id;
    private String _password;
    private String _nickname;
    private String _statusMsg;
    private byte[] _profile;

    private boolean _isOnline;
    private String _groupName; // 소속된 그룹
    private List<User> _buddyList; // 친구 목록
    private List<String> _groupList; // 내가 만든 그룹
    private EasyClient _client;

    public User(int no, String id, String password, String nickname, String statusMsg, byte[] profile) {
        _no = no;
        _id = id;
        _password = password;
        _nickname = nickname;
        _statusMsg = statusMsg;
        _profile = profile;
    }

    /** 내정보 업데이트 **/
    public void update(String nickname, String statusMsg, byte[] profile) {
        _nickname = nickname;
        _statusMsg = statusMsg;
        _profile = profile;

        UsersTable.getInstance().updateUserInfo(this);

    }

    /** 유저 번호 **/
    public int getNo() {
        return _no;
    }

    /** 계정 **/
    public String getId() {
        return _id;
    }

    /** 비밀번호 **/
    public String getPassword() {
        return _password;
    }

    /** 닉네임 **/
    public String getNickname() {
        return _nickname;
    }

    /** 상태 메시지 **/
    public String getStatusMsg() {
        return _statusMsg;
    }

    /** 프로필 사진(파일 바이너리) **/
    public byte[] getProfile() {
        return _profile;
    }

    /** 온라인 여부 **/
    public boolean isOnline() {
        return _isOnline;
    }

    /** 온라인 여부 설정 **/
    public void setOnline(boolean flag) {
        _isOnline = flag;
    }

    /** 친구 그룹 **/
    public String getGroupName() {
        return _groupName;
    }

    /** 그룹 설정 **/
    public void setGroupName(String groupName) {
        _groupName = groupName;
    }

    /** 친구 목록 **/
    public List<User> getBuddyList() {
        return _buddyList;
    }

    /** 친구 추가
     *  @param buddyNo 추가할 친구 번호
     *  @param groupName 그룹명
     *  @param requestMsg 신청 메시지 **/
    public void addBuddy(int buddyNo, String groupName, String requestMsg) {
        User buddy = ObjectManager.getInstance().getUser(buddyNo);
        if (buddy != null) {
            _buddyList.add(buddy);
            UsersBuddyTable.getInstance().addBuddy(_no, buddyNo, groupName);
            UsersBuddyTable.getInstance().addBuddyRequest(buddyNo, _no, requestMsg);
        }
    }

    /** 친구 추가 : 친구 신청에서의 수락
     *  @param isYes True:수락, False:거절
     *  @param buddyNo 추가할 친구 번호
     *  @param groupName 그룹명 **/
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

    /** 친구 삭제
     *  @param buddyNo 삭제할 친구 번호 **/
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

    /** 친구 그룹 변경
     *  @param buddyNo 변경할 친구 번호
     *  @param groupName 변경할 그룹명 **/
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

    /** 내가 만든 그룹 목록 **/
    public List<String> getGroupList() {
        return _groupList;
    }

    /** 내가 만든 그룹 설정 **/
    public void setGroupList(List<String> groupList) {
        _groupList = groupList;
    }

    /** User의 Client 지정 **/
    public void setClient(EasyClient client) {
        _client = client;
    }

    /** 패킷 전송
     *  @param packet 가공된 패킷 객체 **/
    public void sendPacket(WritePacket packet) {
        if (_isOnline && _client != null) {
            _client.sendPacket(packet);
        }
    }

    /** 로그인 처리 **/
    public void login(EasyClient client) {
        _client = client;
        _isOnline = true;
        _buddyList = UsersBuddyTable.getInstance().getBuddys(_no);
        _groupList = UsersBuddyTable.getInstance().getGroups(_no);

        // 내 접속을 친구에게 알림
        for (User buddy : _buddyList) {
            buddy.sendPacket(new S_Buddy(S_Buddy.TYPE_LOGIN, true, this));
        }
    }

    /** 로그아웃  처리 **/
    public void logout() {
        // 내 로그아웃을 친구에게 알림
        for (User buddy : _buddyList) {
            buddy.sendPacket(new S_Buddy(S_Buddy.TYPE_LOGIN, false, this));
        }

        _client = null;
        _isOnline = false;
        _buddyList = null;
        _groupList = null;
    }

}
