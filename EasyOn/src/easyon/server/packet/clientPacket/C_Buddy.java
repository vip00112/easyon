package easyon.server.packet.clientPacket;

import easyon.object.ObjectManager;
import easyon.object.model.User;
import easyon.server.EasyClient;
import easyon.server.packet.serverPacket.S_Buddy;

public class C_Buddy extends ReadPacket {

    private static final int TYPE_ADD = 1; // ģ�� �߰�
    private static final int TYPE_REMOVE = 2; // ģ�� ����
    private static final int TYPE_SEARCH = 3; // ģ�� �˻�
    private static final int TYPE_RESPONE = 4; // ģ�� ��û �亯
    private static final int TYPE_CHANGE_GROUP = 5; // ģ�� �׷� ����

    public C_Buddy(EasyClient client, byte[] packet) {
        super(client, packet);

        int type = readH();

        switch (type) {
        case TYPE_ADD: { // ģ�� �߰�
            int buddyNo = readD();
            String groupName = readS();
            String requestMsg = readS();
            client.getUser().addBuddy(buddyNo, groupName, requestMsg);

            // ��� ������ �������Ͻ� �˸�
            User user = ObjectManager.getInstance().getUser(buddyNo);
            if (user != null && user.isOnline()) {
                user.sendPacket(new S_Buddy(S_Buddy.TYPE_REQUEST, client.getUser(), requestMsg));
            }
            break;
        }

        case TYPE_REMOVE: { // ģ�� ����
            int buddyNo = readD();
            client.getUser().removeBuddy(buddyNo);

            // ��� ������ �������Ͻ� �˸�: �α׾ƿ� ó��
            User user = ObjectManager.getInstance().getUser(buddyNo);
            if (user != null && user.isOnline()) {
                user.sendPacket(new S_Buddy(S_Buddy.TYPE_LOGIN, false, client.getUser()));
            }
            break;
        }

        case TYPE_SEARCH: { // ģ�� �˻�
            String id = readS();
            User user = ObjectManager.getInstance().getUser(id);
            boolean flag = user != null ? true : false;
            client.sendPacket(new S_Buddy(S_Buddy.TYPE_SEARCH_RESULT, flag, user));
            break;
        }

        case TYPE_RESPONE: { // ģ�� ��û �亯
            boolean isYes = readC() == 1 ? true : false;
            int buddyNo = readD();
            String groupName = readS();
            client.getUser().addBuddy(isYes, buddyNo, groupName);

            // ��û ���� �� �� ������ ģ������ �˸�
            if (isYes) {
                User user = ObjectManager.getInstance().getUser(buddyNo);
                if (user != null && user.isOnline()) {
                    user.sendPacket(new S_Buddy(S_Buddy.TYPE_LOGIN, true, client.getUser()));
                    client.getUser().sendPacket(new S_Buddy(S_Buddy.TYPE_LOGIN, true, user));
                }
            }
            break;
        }

        case TYPE_CHANGE_GROUP: { // ģ�� �׷� ����
            int buddyNo = readD();
            String groupName = readS();
            client.getUser().updateGroupName(buddyNo, groupName);
            break;
        }

        }
    }

}
