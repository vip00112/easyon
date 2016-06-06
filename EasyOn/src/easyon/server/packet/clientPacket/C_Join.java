package easyon.server.packet.clientPacket;

import easyon.db.UsersTable;
import easyon.object.ObjectManager;
import easyon.object.model.User;
import easyon.server.EasyClient;
import easyon.server.packet.serverPacket.S_JoinResult;

public class C_Join extends ReadPacket {

    public C_Join(EasyClient client, byte[] packet) {
        super(client, packet);

        String id = readS();
        String password = readS();
        System.out.println("ȸ������ ��û = " + id + " / " + password);

        // ID �ߺ� Ȯ��
        if (ObjectManager.getInstance().getUser(id) != null) {
            client.sendPacket(new S_JoinResult(S_JoinResult.TYPE_USED));
            return;
        }
        
        User user = UsersTable.getInstance().join(id, password);
        if (user != null) {
            ObjectManager.getInstance().addUser(user);
            client.sendPacket(new S_JoinResult(S_JoinResult.TYPE_OK));
        }
    }

}
