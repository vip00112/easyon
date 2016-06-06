package easyon.server.packet.clientPacket;

import java.sql.Timestamp;
import java.util.List;

import easyon.db.MemoTable;
import easyon.object.ObjectManager;
import easyon.object.model.Memo;
import easyon.object.model.User;
import easyon.server.EasyClient;
import easyon.server.packet.serverPacket.S_Memo;

public class C_Memo extends ReadPacket {

    private static final int TYPE_MSG = 1; // ���� ����
    private static final int TYPE_LIST = 2; // ���� ���� ���
    private static final int TYPE_READ = 3; // ���� ���� ����

    public C_Memo(EasyClient client, byte[] packet) {
        super(client, packet);

        int type = readH();

        switch (type) {
        case TYPE_MSG: { // �޽��� ����
            int readerNo = readD(); // ��� ���� ��ȣ
            String msg = readS();
            Timestamp regdate = new Timestamp(System.currentTimeMillis());

            Memo memo = new Memo(client.getUser().getNo(), readerNo, msg, false, regdate);
            User buddy = ObjectManager.getInstance().getUser(readerNo);
            if (buddy != null && buddy.isOnline()) {
                memo.setWriter(client.getUser().getId(), client.getUser().getNickname());
                memo.setRead(true);
                buddy.sendPacket(new S_Memo(S_Memo.TYPE_MSG, memo));
            }
            MemoTable.getInstance().writeMemo(memo);
            break;
        }

        case TYPE_LIST: { // ���� ���� ���
            int writerNo = readD();

            List<Memo> memoList = MemoTable.getInstance().getMemoList(writerNo, client.getUser().getNo());
            client.sendPacket(new S_Memo(S_Memo.TYPE_LIST, memoList));
            break;
        }

        case TYPE_READ: { // ���� ���� ����
            int no = readD();
            MemoTable.getInstance().readMemo(no);

            int count = MemoTable.getInstance().getNotReadCount(client.getUser().getNo());
            client.sendPacket(new S_Memo(S_Memo.TYPE_COUNT, count));
            break;
        }

        }
    }

}
