package easyon.server;

/** 패킷의 KEY **/
public class Opcodes {
    // Client Opcode
    public static final int C_LOGIN = 1; // 로그인 정보
    public static final int C_BUDDY = 2; // 친구 관련
    public static final int C_UPDATE_MYINFO = 3; // 내 정보 수정
    public static final int C_UPDATE_GROUP = 4; // 그룹 정보 수정
    public static final int C_MEMO = 5; // 메모 관련
    public static final int C_JOIN = 6; // 회원가입

    // Server Opcode
    public static final int S_LOGIN_RESULT = 1; // 로그인 결과
    public static final int S_MYINFO = 2; // 내정보
    public static final int S_BUDDY = 3; // 친구 관련
    public static final int S_MEMO = 4; // 메모 관련
    public static final int S_JOIN_RESULT = 5; // 회원가입 결과
}
