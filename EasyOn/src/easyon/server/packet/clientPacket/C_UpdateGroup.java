package easyon.server.packet.clientPacket;

import java.util.ArrayList;
import java.util.List;

import easyon.db.UsersBuddyTable;
import easyon.server.EasyClient;

public class C_UpdateGroup extends ReadPacket {

    public C_UpdateGroup(EasyClient client, byte[] packet) {
        super(client, packet);

        List<String> groups = new ArrayList<String>();
        int totalCount = readH();
        for (int i = 0; i < totalCount; i++) {
            String groupName = readS();
            if (!groups.contains(groupName)) {
                groups.add(groupName);
            }
        }

        client.getUser().setGroupList(groups);
        UsersBuddyTable.getInstance().updateGroups(client.getUser().getNo(), groups);
    }

}
