package easyon.server.packet.clientPacket;

import java.util.ArrayList;
import java.util.List;

import easyon.db.ChatTable;
import easyon.object.ObjectManager;
import easyon.object.model.Chat;
import easyon.object.model.ChatLog;
import easyon.object.model.User;
import easyon.server.EasyClient;
import easyon.server.packet.serverPacket.S_Chat;

public class C_Chat extends ReadPacket {

    private static final int TYPE_OPEN = 1; // ä��â ����
    private static final int TYPE_LOG = 2; // ���� ��ȭ ���
    private static final int TYPE_MSG = 3; // �޽��� ����

    public C_Chat(EasyClient client, byte[] packet) {
        super(client, packet);

        int type = readH();

        switch (type) {
        case TYPE_OPEN: { // ä��â ����
            int size = readH();
            List<User> memberList = new ArrayList<User>();
            for (int i = 0; i < size; i++) {
                int memberNo = readD();
                User member = ObjectManager.getInstance().getUser(memberNo);
                if (member != null) {
                    memberList.add(member);
                }
            }
            Chat chat = ChatTable.getInstance().addChat(memberList);
            client.sendPacket(new S_Chat(S_Chat.TYPE_ADD, chat));
            break;
        }

        case TYPE_LOG: { // ���� ��ȭ ���
            int chatNo = readD();
            List<ChatLog> chatLogList = ChatTable.getInstance().getChatLogs(chatNo);
            for (ChatLog log : chatLogList) {
                client.sendPacket(new S_Chat(S_Chat.TYPE_LOG, log));
            }
            break;
        }

        case TYPE_MSG: { // �޽��� ����
            int chatNo = readD();
            String msg = readS();
            ChatTable.getInstance().addChatLog(chatNo, client.getUser(), msg);
            break;
        }

        }

    }

}
