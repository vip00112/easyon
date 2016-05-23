package easyon.server.packet.serverPacket;

import easyon.object.model.User;
import easyon.server.Opcodes;

/** ���� ����(������) **/
public class S_MyInfo extends SendPacket {

    public S_MyInfo(User user) {
        writeC(Opcodes.S_MYINFO);
        writeD(user.getNo());
        writeS(user.getId());
        writeS(user.getPassword());
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

}
