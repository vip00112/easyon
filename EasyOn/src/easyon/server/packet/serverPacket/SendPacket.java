package easyon.server.packet.serverPacket;

import java.io.ByteArrayOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

public abstract class SendPacket {
    private static final Logger _log = Logger.getLogger(SendPacket.class.getName());

    private int _opcode; // opcode Key
    private boolean _isKey = true;

    protected ByteArrayOutputStream _bao = new ByteArrayOutputStream();

    protected void writeC(int value) {
        _bao.write(value & 0xff);
        if (_isKey) {
            _isKey = false;
            _opcode = value;
        }
    }

    protected void writeH(int value) {
        _bao.write(value & 0xff);
        _bao.write(value >> 8 & 0xff);
    }

    protected void writeD(int value) {
        _bao.write(value & 0xff);
        _bao.write(value >> 8 & 0xff);
        _bao.write(value >> 16 & 0xff);
        _bao.write(value >> 24 & 0xff);
    }

    protected void writeS(String text) {
        try {
            if (text != null) {
                _bao.write(text.getBytes("UTF-8"));
            }
        } catch (Exception e) {
            _log.log(Level.WARNING, "", e);
        }
        _bao.write(0);
    }

    protected void writeBytes(byte[] data) {
        try {
            if (data != null) {
                _bao.write(data);
            }
        } catch (Exception e) {
            _log.log(Level.WARNING, "", e);
        }
    }

    /** channel로 패킷을 전송하기 위한 가공
     *  @param channel 
     *  @param data 실제 데이터
     *  @param length 길이값이 포함된 클라이언트로 보낼 배열 크기
     *  @return io.netty.buffer.ByteBuf **/
    public ByteBuf getByteBuf(Channel channel, byte[] data, int length) {
        ByteBuf buf = null;
        try {
            buf = channel.alloc().buffer();
            byte[] size = new byte[2];
            size[0] |= length & 0xff;
            size[1] |= length >> 8 & 0xff;
            buf.writeBytes(size);
            buf.writeBytes(data);
        } catch (Exception e) {
            _log.log(Level.WARNING, "", e);
        }
        return buf;
    }

    /** 길이값이 포함된 클라이언트로 보낼 배열 크기 **/
    public int getLength() {
        return _bao.size() + 2;
    }

    /** 클라이언트로 실제 데이터 **/
    public byte[] getBytes() {
        return _bao.toByteArray();
    }

    @Override
    public String toString() {
        return "S_" + _opcode;
    }

}
