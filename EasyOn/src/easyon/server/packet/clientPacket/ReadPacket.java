package easyon.server.packet.clientPacket;

import java.util.logging.Level;
import java.util.logging.Logger;

import easyon.server.EasyClient;

public abstract class ReadPacket {
    private static final Logger _log = Logger.getLogger(ReadPacket.class.getName());

    private byte[] _data;
    private int _offset;

    public ReadPacket(EasyClient client, byte[] packet) {
        _data = packet;
        _offset = 1;
    }

    protected int readC() {
        int result = 0;
        try {
            result = _data[_offset++] & 0xff;
        } catch (Exception e) {
            _log.log(Level.WARNING, "", e);
        }
        return result;
    }

    protected int readH() {
        int result = 0;
        try {
            result = _data[_offset++] & 0xff;
            result |= _data[_offset++] << 8 & 0xff00;
        } catch (Exception e) {
            _log.log(Level.WARNING, "", e);
        }
        return result;
    }

    protected int readD() {
        int result = 0;
        try {
            result = _data[_offset++] & 0xff;
            result |= _data[_offset++] << 8 & 0xff00;
            result |= _data[_offset++] << 16 & 0xff0000;
            result |= _data[_offset++] << 24 & 0xff000000;
        } catch (Exception e) {
            _log.log(Level.WARNING, "", e);
        }
        return result;
    }

    protected byte[] readBytes() {
        byte[] result = new byte[_data.length - _offset];
        try {
            System.arraycopy(_data, _offset, result, 0, _data.length - _offset);
            _offset = _data.length;
        } catch (Exception e) {
            _log.log(Level.WARNING, "", e);
        }
        return result;
    }

    public byte[] readBytes(int length) {
        byte[] result = new byte[length];
        try {
            System.arraycopy(_data, _offset, result, 0, length);
            _offset = _data.length;
        } catch (Exception e) {
            _log.log(Level.WARNING, "", e);
        }
        return result;
    }

    protected String readS() {
        String result = "";
        try {
            int length = 0;
            int index = _offset;
            while (index < _data.length && _data[index++] != 0) {
                length++;
            }
            result = new String(_data, _offset, length, "UTF-8");
            _offset += length + 1;
        } catch (Exception e) {
            _log.log(Level.WARNING, "", e);
        }
        return result;
    }

}
