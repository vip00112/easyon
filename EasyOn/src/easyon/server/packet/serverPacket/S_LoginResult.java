package easyon.server.packet.serverPacket;

import easyon.server.Opcodes;

/** �α��� ��� **/
public class S_LoginResult extends WritePacket {

    public static final int TYPE_OK = 1; // �α��� ����
    public static final int TYPE_WRONG = 2; // �߸��� ���� ����
    public static final int TYPE_USED = 3; // �̹� �����

    public S_LoginResult(int type) {
        writeC(Opcodes.S_LOGIN_RESULT);
        writeC(type);
    }

}
