package easyon.server;

import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import easyon.config.Config;
import easyon.object.ObjectManager;
import easyon.object.model.User;
import easyon.server.packet.clientPacket.C_Buddy;
import easyon.server.packet.clientPacket.C_Login;
import easyon.server.packet.clientPacket.C_Chat;
import easyon.server.packet.clientPacket.C_UpdateGroup;
import easyon.server.packet.clientPacket.C_UpdateMyInfo;
import easyon.server.packet.serverPacket.SendPacket;
import easyon.util.CommonUtil;
import easyon.util.GeneralThreadPool;
import io.netty.channel.ChannelHandlerContext;

/** Client ���� **/
public class EasyClient {
    private static final Logger _log = Logger.getLogger(EasyClient.class.getName());

    public static final String CLIENT_KEY = "EasyOn Client";
    public static final int MAX_SIZE = 1024 * 1024; // ���� ���� �ִ� ũ��

    private ChannelHandlerContext _ctx;
    private String _ip;
    private boolean _isClosed;
    private byte[] _packetData;
    private int _packetIdx;
    private User _user;

    public EasyClient(ChannelHandlerContext ctx) {
        _ctx = ctx;
        if (ctx != null && ctx.channel().remoteAddress() != null) {
            StringTokenizer st = new StringTokenizer(ctx.channel().remoteAddress().toString().substring(1), ":");
            _ip = st.nextToken();
        }
        _packetData = new byte[MAX_SIZE];
        _packetIdx = 0;

        // ��ȿ ��Ŷ ���� Thread
        GeneralThreadPool.getInstance().execute(new PacketFactory());
    }

    /** Client�� IP **/
    public String getIp() {
        return _ip;
    }

    /** ����ó�� �� socket���� ���� **/
    public void close() {
        if (!_isClosed) {
            _isClosed = true;

            // �α׾ƿ� ó��
            if (_user != null) {
                ObjectManager.getInstance().removeLoginUser(_user.getNo());
            }

            // socket ��������
            _ctx.channel().close();

            if (Config.WRITE_SOCKET_LOG) {
                CommonUtil.writeCommandLine("++ Client ���� : " + _ip);
            }
        }
    }

    /** ��Ŷ�� ����� byte �迭 **/
    public byte[] getPacketData() {
        return _packetData;
    }

    /** packetData���� �����͸� ���� ���� ��ġ **/
    public int getPacketIdx() {
        return _packetIdx;
    }

    /** packetData���� �����͸� ���� ��ġ ����
     *  @param packetIdx **/
    public void addPacketIdx(int packetIdx) {
        this._packetIdx += packetIdx;
    }

    /** Client�� ����� ���� **/
    public User getUser() {
        return _user;
    }

    /** Client�� ����� ���� **/
    public void setUser(User user) {
        _user = user;
    }

    /** Client�� ���� ���� **/
    public boolean isConnected() {
        return _ctx.channel().isActive();
    }

    /** ��Ŷ ����
     *  @param packet ������ ��Ŷ ��ü **/
    public void sendPacket(SendPacket packet) {
        byte[] data = packet.getBytes();
        if (data != null) {
            _ctx.writeAndFlush(packet.getByteBuf(_ctx.channel(), data, packet.getLength()));
        }
    }

    // Client Packet ó��
    private void packetHandler(byte[] data) {
        if (data == null) {
            return;
        }

        // ���� ��Ŷ���� ũ�Ⱚ�� ������ �κ� ���
        int size = getSize(data) - 2;
        byte[] packet = new byte[size];
        System.arraycopy(data, 2, packet, 0, size);

        int opcode = packet[0] & 0xFF;
        //System.out.println("EasyClient Opcode : " + opcode);
        switch (opcode) {
        case Opcodes.C_LOGIN: // �α��� ����
            new C_Login(this, packet);
            break;
        case Opcodes.C_BUDDY: // ģ�� ����
            new C_Buddy(this, packet);
            break;
        case Opcodes.C_UPDATE_MYINFO: // �� ���� ����
            new C_UpdateMyInfo(this, packet);
            break;
        case Opcodes.C_UPDATE_GROUP: // �׷� ���� ����
            new C_UpdateGroup(this, packet);
            break;
        case Opcodes.C_CHAT: // ä�� ����
            new C_Chat(this, packet);
            break;
        }
    }

    // ��Ŷ ũ�� ��ȯ
    private int getSize(byte[] data) {
        int size = data[0] & 0xff;
        size |= data[1] << 8 & 0xff00;
        return size;
    }

    // ��ȿ ��Ŷ ���� Thread
    private class PacketFactory implements Runnable {
        @Override
        public void run() {
            try {
                while (isConnected()) {
                    if (_packetData != null) {
                        int size = getSize(_packetData);
                        if (size != 0 && size <= _packetIdx) {
                            byte[] data = new byte[size];
                            System.arraycopy(_packetData, 0, data, 0, size);
                            System.arraycopy(_packetData, size, _packetData, 0, _packetIdx - size);
                            _packetIdx -= size;
                            packetHandler(data);
                        }
                    }
                    Thread.sleep(20);
                }
            } catch (Exception e) {
                _log.log(Level.WARNING, "", e);
            } finally {
                close();
            }
        }
    }

}
