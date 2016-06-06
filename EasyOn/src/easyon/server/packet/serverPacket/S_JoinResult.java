package easyon.server.packet.serverPacket;

import easyon.server.Opcodes;

public class S_JoinResult extends WritePacket {

    public static final int TYPE_OK = 1; // 가입 성공
    public static final int TYPE_USED = 2; // 이미 존재하는 아이디

    public S_JoinResult(int type) {
        writeC(Opcodes.S_JOIN_RESULT);
        writeC(type);
    }

}