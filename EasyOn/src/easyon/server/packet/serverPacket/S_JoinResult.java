package easyon.server.packet.serverPacket;

import easyon.server.Opcodes;

public class S_JoinResult extends WritePacket {

    public static final int TYPE_OK = 1; // ���� ����
    public static final int TYPE_USED = 2; // �̹� �����ϴ� ���̵�

    public S_JoinResult(int type) {
        writeC(Opcodes.S_JOIN_RESULT);
        writeC(type);
    }

}