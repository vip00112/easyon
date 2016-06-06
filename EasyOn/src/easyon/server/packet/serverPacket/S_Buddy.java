package easyon.server.packet.serverPacket;

import java.util.List;
import java.util.Map;

import easyon.object.model.User;
import easyon.server.Opcodes;

/** ģ�� ���� **/
public class S_Buddy extends WritePacket {

    public static final int TYPE_INFO = 1; // �� ���� ����
    public static final int TYPE_LIST = 2; // ģ�� ���
    public static final int TYPE_LOGIN = 3; // �� �α���/�α׾ƿ�
    public static final int TYPE_REQUEST = 4; // ģ�� ��û
    public static final int TYPE_REQUEST_LIST = 5; // ģ�� ��û ���
    public static final int TYPE_SEARCH_RESULT = 6; // ģ�� �˻� ���
    public static final int TYPE_GROUPS = 7; // �׷� ���

    /** TYPE_INFO, TYPE_LIST **/
    public S_Buddy(int type, User user) {
        writeC(Opcodes.S_BUDDY);
        writeH(type);

        switch (type) {
        case TYPE_INFO: { // �� ���� ����
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

        case TYPE_LIST: { // ģ�� ���
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

        case TYPE_GROUPS: { // �׷� ���
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
        case TYPE_LOGIN: { // �� �α���/�α׾ƿ�
            writeC(flag ? 1 : 0); // 1:�α���, 0:�α׾ƿ�
            writeS(user.getId());
            break;
        }

        case TYPE_SEARCH_RESULT: { // ģ�� �˻� ���
            writeC(flag ? 1 : 0); // 1:��� ����, 0:��� ����

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
        case TYPE_REQUEST: { // ģ�� ��û
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
        case TYPE_REQUEST_LIST: { // ģ�� ��û ���
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
