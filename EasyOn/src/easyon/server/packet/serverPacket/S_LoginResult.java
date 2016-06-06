package easyon.server.packet.serverPacket;

import easyon.server.Opcodes;

/** 로그인 결과 **/
public class S_LoginResult extends WritePacket {

    public static final int TYPE_OK = 1; // 로그인 성공
    public static final int TYPE_WRONG = 2; // 잘못된 계정 정보
    public static final int TYPE_USED = 3; // 이미 사용중

    public S_LoginResult(int type) {
        writeC(Opcodes.S_LOGIN_RESULT);
        writeC(type);
    }

}
