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

/** Client 정보 **/
public class EasyClient {
    private static final Logger _log = Logger.getLogger(EasyClient.class.getName());

    public static final String CLIENT_KEY = "EasyOn Client";
    public static final int MAX_SIZE = 1024 * 1024; // 수신 버퍼 최대 크기

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

        // 유효 패킷 가공 Thread
        GeneralThreadPool.getInstance().execute(new PacketFactory());
    }

    /** Client의 IP **/
    public String getIp() {
        return _ip;
    }

    /** 종료처리 및 socket연결 해제 **/
    public void close() {
        if (!_isClosed) {
            _isClosed = true;

            // 로그아웃 처리
            if (_user != null) {
                ObjectManager.getInstance().removeLoginUser(_user.getNo());
            }

            // socket 연결해제
            _ctx.channel().close();

            if (Config.WRITE_SOCKET_LOG) {
                CommonUtil.writeCommandLine("++ Client 종료 : " + _ip);
            }
        }
    }

    /** 패킷이 저장된 byte 배열 **/
    public byte[] getPacketData() {
        return _packetData;
    }

    /** packetData에서 데이터를 읽을 현재 위치 **/
    public int getPacketIdx() {
        return _packetIdx;
    }

    /** packetData에서 데이터를 읽을 위치 변경
     *  @param packetIdx **/
    public void addPacketIdx(int packetIdx) {
        this._packetIdx += packetIdx;
    }

    /** Client의 사용자 정보 **/
    public User getUser() {
        return _user;
    }

    /** Client의 사용자 지정 **/
    public void setUser(User user) {
        _user = user;
    }

    /** Client의 접속 여부 **/
    public boolean isConnected() {
        return _ctx.channel().isActive();
    }

    /** 패킷 전송
     *  @param packet 가공된 패킷 객체 **/
    public void sendPacket(SendPacket packet) {
        byte[] data = packet.getBytes();
        if (data != null) {
            _ctx.writeAndFlush(packet.getByteBuf(_ctx.channel(), data, packet.getLength()));
        }
    }

    // Client Packet 처리
    private void packetHandler(byte[] data) {
        if (data == null) {
            return;
        }

        // 원본 패킷에서 크기값을 제외한 부분 취득
        int size = getSize(data) - 2;
        byte[] packet = new byte[size];
        System.arraycopy(data, 2, packet, 0, size);

        int opcode = packet[0] & 0xFF;
        //System.out.println("EasyClient Opcode : " + opcode);
        switch (opcode) {
        case Opcodes.C_LOGIN: // 로그인 관련
            new C_Login(this, packet);
            break;
        case Opcodes.C_BUDDY: // 친구 관련
            new C_Buddy(this, packet);
            break;
        case Opcodes.C_UPDATE_MYINFO: // 내 정보 수정
            new C_UpdateMyInfo(this, packet);
            break;
        case Opcodes.C_UPDATE_GROUP: // 그룹 정보 변경
            new C_UpdateGroup(this, packet);
            break;
        case Opcodes.C_CHAT: // 채팅 관련
            new C_Chat(this, packet);
            break;
        }
    }

    // 패킷 크기 반환
    private int getSize(byte[] data) {
        int size = data[0] & 0xff;
        size |= data[1] << 8 & 0xff00;
        return size;
    }

    // 유효 패킷 가공 Thread
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
