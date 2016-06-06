package easyon.server.packet.clientPacket;

import java.util.Map;

import easyon.db.MemoTable;
import easyon.db.UsersBuddyTable;
import easyon.object.ObjectManager;
import easyon.object.model.User;
import easyon.server.EasyClient;
import easyon.server.packet.serverPacket.S_Buddy;
import easyon.server.packet.serverPacket.S_LoginResult;
import easyon.server.packet.serverPacket.S_Memo;
import easyon.server.packet.serverPacket.S_MyInfo;

/** Client의 로그인 요청 <br>
 *  로그인 답변, 유저정보, 친구목록 까지 관할 **/
public class C_Login extends ReadPacket {

    public C_Login(EasyClient client, byte[] packet) {
        super(client, packet);

        String id = readS();
        String password = readS();
        System.out.println("로그인 요청 = " + id + " / " + password);

        User user = ObjectManager.getInstance().getUserByLogin(id, password);
        if (user != null) {
            if (user.isOnline()) {
                client.sendPacket(new S_LoginResult(S_LoginResult.TYPE_USED));
                return;
            }

            client.setUser(user);
            user.login(client);

            client.sendPacket(new S_LoginResult(S_LoginResult.TYPE_OK));
            client.sendPacket(new S_MyInfo(user));
            client.sendPacket(new S_Buddy(S_Buddy.TYPE_GROUPS, user));
            client.sendPacket(new S_Buddy(S_Buddy.TYPE_LIST, user));
            Map<User, String> buddyRequests = UsersBuddyTable.getInstance().getBuddyRequests(user);
            if (buddyRequests.size() > 0) {
                client.sendPacket(new S_Buddy(S_Buddy.TYPE_REQUEST_LIST, buddyRequests));
            }
            int count = MemoTable.getInstance().getNotReadCount(user.getNo());
            client.sendPacket(new S_Memo(S_Memo.TYPE_COUNT, count));
        } else {
            client.sendPacket(new S_LoginResult(S_LoginResult.TYPE_WRONG));
        }
    }

}
