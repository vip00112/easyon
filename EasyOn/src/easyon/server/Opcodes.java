package easyon.server;

/** 패킷의 KEY **/
public class Opcodes {
    // Client Opcode
    public static final int C_LOGIN = 1; // 로그인 정보
    public static final int C_BUDDY = 2; // 친구 관련
    public static final int C_UPDATE_MYINFO = 3; // 내 정보 수정
    public static final int C_UPDATE_GROUP = 4; // 그룹 정보 수정
    public static final int C_CHAT = 5; // 채팅 관련

    // Server Opcode
    public static final int S_LOGIN_RESULT = 1; // 로그인 결과
    public static final int S_MYINFO = 2; // 내정보
    public static final int S_BUDDY = 3; // 친구 관련
    public static final int S_CHAT = 4; // 채팅 관련
}
