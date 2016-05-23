package easyon.server.packet.clientPacket;

import easyon.object.ObjectManager;
import easyon.object.model.User;
import easyon.server.EasyClient;
import easyon.server.packet.serverPacket.S_Buddy;

public class C_Buddy extends ReadPacket {

    private static final int TYPE_ADD = 1; // 친구 추가
    private static final int TYPE_REMOVE = 2; // 친구 삭제
    private static final int TYPE_SEARCH = 3; // 친구 검색
    private static final int TYPE_RESPONE = 4; // 친구 신청 답변
    private static final int TYPE_CHANGE_GROUP = 5; // 친구 그룹 변경

    public C_Buddy(EasyClient client, byte[] packet) {
        super(client, packet);

        int type = readH();

        switch (type) {
        case TYPE_ADD: { // 친구 추가
            int buddyNo = readD();
            String groupName = readS();
            String requestMsg = readS();
            client.getUser().addBuddy(buddyNo, groupName, requestMsg);

            // 대상 유저가 접속중일시 알림
            User user = ObjectManager.getInstance().getUser(buddyNo);
            if (user != null && user.isOnline()) {
                user.sendPacket(new S_Buddy(S_Buddy.TYPE_REQUEST, client.getUser(), requestMsg));
            }
            break;
        }

        case TYPE_REMOVE: { // 친구 삭제
            int buddyNo = readD();
            client.getUser().removeBuddy(buddyNo);

            // 대상 유저가 접속중일시 알림: 로그아웃 처리
            User user = ObjectManager.getInstance().getUser(buddyNo);
            if (user != null && user.isOnline()) {
                user.sendPacket(new S_Buddy(S_Buddy.TYPE_LOGIN, false, client.getUser()));
            }
            break;
        }

        case TYPE_SEARCH: { // 친구 검색
            String id = readS();
            User user = ObjectManager.getInstance().getUser(id);
            boolean flag = user != null ? true : false;
            client.sendPacket(new S_Buddy(S_Buddy.TYPE_SEARCH_RESULT, flag, user));
            break;
        }

        case TYPE_RESPONE: { // 친구 신청 답변
            boolean isYes = readC() == 1 ? true : false;
            int buddyNo = readD();
            String groupName = readS();
            client.getUser().addBuddy(isYes, buddyNo, groupName);

            // 신청 수락 후 내 접속을 친구에게 알림
            if (isYes) {
                User user = ObjectManager.getInstance().getUser(buddyNo);
                if (user != null && user.isOnline()) {
                    user.sendPacket(new S_Buddy(S_Buddy.TYPE_LOGIN, true, client.getUser()));
                    client.getUser().sendPacket(new S_Buddy(S_Buddy.TYPE_LOGIN, true, user));
                }
            }
            break;
        }

        case TYPE_CHANGE_GROUP: { // 친구 그룹 변경
            int buddyNo = readD();
            String groupName = readS();
            client.getUser().updateGroupName(buddyNo, groupName);
            break;
        }

        }
    }

}
