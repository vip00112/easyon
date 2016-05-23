package easyon.server.packet.clientPacket;

import java.util.List;

import easyon.object.model.User;
import easyon.server.EasyClient;
import easyon.server.packet.serverPacket.S_Buddy;

/** Client�� �� ���� ���� ��û **/
public class C_UpdateMyInfo extends ReadPacket {

    public C_UpdateMyInfo(EasyClient client, byte[] packet) {
        super(client, packet);

        User user = client.getUser();
        String nickname = readS();
        String statusMsg = readS();
        int fileSize = readH();
        byte[] profile = fileSize > 0 ? readBytes(fileSize) : null;
        user.update(nickname, statusMsg, profile);

        // �������� ģ������ �� ���� ���� �˸�
        List<User> buddys = user.getBuddyList();
        for (User buddy : buddys) {
            buddy.sendPacket(new S_Buddy(S_Buddy.TYPE_INFO, user));
        }
    }

}
