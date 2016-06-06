package easyon.server.packet.serverPacket;

import java.util.List;

import easyon.object.model.Memo;
import easyon.server.Opcodes;

public class S_Memo extends WritePacket {

    public static final int TYPE_MSG = 1; // 쪽지 수신
    public static final int TYPE_LIST = 2; // 수신 쪽지 목록
    public static final int TYPE_COUNT = 3; // 미확인 쪽지 갯수

    public S_Memo(int type, Memo memo) {
        writeC(Opcodes.S_MEMO);
        writeH(type);

        switch (type) {
        case TYPE_MSG: { // 쪽지 수신
            writeD(memo.getNo());
            writeD(memo.getWriterNo());
            writeS(memo.getWriterId());
            writeS(memo.getWriterNickname());
            writeD(memo.getReaderNo());
            writeS(memo.getMsg());
            writeC(memo.isRead() ? 1 : 0);
            writeD((int) (memo.getRegdate().getTime() / 1000));
            break;
        }
        }
    }

    public S_Memo(int type, List<Memo> memoList) {
        writeC(Opcodes.S_MEMO);
        writeH(type);

        switch (type) {
        case TYPE_LIST: { // 수신 쪽지 목록
            writeH(memoList.size());
            for (Memo memo : memoList) {
                writeD(memo.getNo());
                writeD(memo.getWriterNo());
                writeS(memo.getWriterId());
                writeS(memo.getWriterNickname());
                writeD(memo.getReaderNo());
                writeS(memo.getMsg());
                writeC(memo.isRead() ? 1 : 0);
                writeD((int) (memo.getRegdate().getTime() / 1000));
            }
            break;
        }
        }

    }

    public S_Memo(int type, int count) {
        writeC(Opcodes.S_MEMO);
        writeH(type);

        switch (type) {
        case TYPE_COUNT: { // 미확인 쪽지 갯수
            writeH(count);
            break;
        }
        }

    }

}
