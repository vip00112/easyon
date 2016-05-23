package easyon.server;

/** ��Ŷ�� KEY **/
public class Opcodes {
    // Client Opcode
    public static final int C_LOGIN = 1; // �α��� ����
    public static final int C_BUDDY = 2; // ģ�� ����
    public static final int C_UPDATE_MYINFO = 3; // �� ���� ����
    public static final int C_UPDATE_GROUP = 4; // �׷� ���� ����
    public static final int C_CHAT = 5; // ä�� ����

    // Server Opcode
    public static final int S_LOGIN_RESULT = 1; // �α��� ���
    public static final int S_MYINFO = 2; // ������
    public static final int S_BUDDY = 3; // ģ�� ����
    public static final int S_CHAT = 4; // ä�� ����
}
