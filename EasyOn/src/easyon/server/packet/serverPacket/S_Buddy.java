package easyon.server.packet.serverPacket;

import java.util.List;
import java.util.Map;

import easyon.object.model.User;
import easyon.server.Opcodes;

/** 친구 관련 **/
public class S_Buddy extends WritePacket {

    public static final int TYPE_INFO = 1; // 내 정보 변경
    public static final int TYPE_LIST = 2; // 친구 목록
    public static final int TYPE_LOGIN = 3; // 내 로그인/로그아웃
    public static final int TYPE_REQUEST = 4; // 친구 신청
    public static final int TYPE_REQUEST_LIST = 5; // 친구 신청 목록
    public static final int TYPE_SEARCH_RESULT = 6; // 친구 검색 결과
    public static final int TYPE_GROUPS = 7; // 그룹 목록

    /** TYPE_INFO, TYPE_LIST **/
    public S_Buddy(int type, User user) {
        writeC(Opcodes.S_BUDDY);
        writeH(type);

        switch (type) {
        case TYPE_INFO: { // 내 정보 변경
            writeD(user.getNo());
            writeS(user.getId());
            writeS(user.getNickname());
            writeS(user.getStatusMsg());
            byte[] profile = user.getProfile();
            if (profile != null) {
                writeH(profile.length);
                writeBytes(profile);
            } else {
                writeH(0);
            }
            writeC(user.isOnline() ? 1 : 0);
            break;
        }

        case TYPE_LIST: { // 친구 목록
            List<User> buddyList = user.getBuddyList();
            writeH(buddyList.size()); // totalCount
            for (User buddy : buddyList) {
                writeD(buddy.getNo());
                writeS(buddy.getId());
                writeS(buddy.getNickname());
                writeS(buddy.getStatusMsg());
                byte[] profile = buddy.getProfile();
                if (profile != null) {
                    writeH(profile.length);
                    writeBytes(profile);
                } else {
                    writeH(0);
                }
                writeC(buddy.isOnline() ? 1 : 0);
                writeS(buddy.getGroupName());
            }
            break;
        }

        case TYPE_GROUPS: { // 그룹 목록
            List<String> groupList = user.getGroupList();
            writeH(groupList.size()); // totalCount
            for (String group : groupList) {
                writeS(group);
            }
            break;
        }

        }
    }

    /** TYPE_LOGIN, TYPE_SEARCH_RESULT **/
    public S_Buddy(int type, boolean flag, User user) {
        writeC(Opcodes.S_BUDDY);
        writeH(type);

        switch (type) {
        case TYPE_LOGIN: { // 내 로그인/로그아웃
            writeC(flag ? 1 : 0); // 1:로그인, 0:로그아웃
            writeS(user.getId());
            break;
        }

        case TYPE_SEARCH_RESULT: { // 친구 검색 결과
            writeC(flag ? 1 : 0); // 1:결과 존재, 0:결과 없음

            if (flag) {
                writeD(user.getNo());
                writeS(user.getId());
                writeS(user.getNickname());
                writeS(user.getStatusMsg());
                byte[] profile = user.getProfile();
                if (profile != null) {
                    writeH(profile.length);
                    writeBytes(profile);
                } else {
                    writeH(0);
                }
            }
            break;
        }

        }
    }

    /** TYPE_REQUEST **/
    public S_Buddy(int type, User user, String requestMsg) {
        writeC(Opcodes.S_BUDDY);
        writeH(type);

        switch (type) {
        case TYPE_REQUEST: { // 친구 신청
            writeD(user.getNo());
            writeS(user.getId());
            writeS(user.getNickname());
            writeS(user.getStatusMsg());
            byte[] profile = user.getProfile();
            if (profile != null) {
                writeH(profile.length);
                writeBytes(profile);
            } else {
                writeH(0);
            }
            writeS(requestMsg);
            break;
        }
        }
    }

    /** TYPE_REQUEST_LIST **/
    public S_Buddy(int type, Map<User, String> buddyRequests) {
        writeC(Opcodes.S_BUDDY);
        writeH(type);

        switch (type) {
        case TYPE_REQUEST_LIST: { // 친구 신청 목록
            writeH(buddyRequests.size()); // totalCount
            for (User user : buddyRequests.keySet()) {
                writeD(user.getNo());
                writeS(user.getId());
                writeS(user.getNickname());
                writeS(user.getStatusMsg());
                byte[] profile = user.getProfile();
                if (profile != null) {
                    writeH(profile.length);
                    writeBytes(profile);
                } else {
                    writeH(0);
                }
                writeS(buddyRequests.get(user)); // requestMsg
            }
            break;
        }
        }
    }

}
