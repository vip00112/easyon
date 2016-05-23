package easyon.server.packet.serverPacket;

import java.util.List;

import easyon.object.model.Chat;
import easyon.object.model.ChatLog;
import easyon.object.model.User;
import easyon.server.Opcodes;

public class S_Chat extends SendPacket {

    public static final int TYPE_LIST = 1; // 채팅 목록
    public static final int TYPE_LOG = 2; // 이전 대화 목록
    public static final int TYPE_ADD = 3; // 새로운 채팅

    public S_Chat(int type, User user) {
        writeC(Opcodes.S_CHAT);
        writeH(type);

        switch (type) {
        case TYPE_LIST: { // 채팅 목록
            List<Chat> chatList = user.getChatList();
            writeH(chatList.size());
            for (Chat chat : chatList) {
                writeD(chat.getNo());
                writeD((int) (chat.getRegdate().getTime() / 1000));

                List<User> memberList = chat.getMemberList();
                writeH(memberList.size());
                for (User member : memberList) {
                    writeD(member.getNo());
                    writeS(member.getId());
                    writeS(member.getNickname());
                    writeS(member.getStatusMsg());
                    byte[] profile = member.getProfile();
                    if (profile != null) {
                        writeH(profile.length);
                        writeBytes(profile);
                    } else {
                        writeH(0);
                    }
                }
            }
            break;
        }

        }
    }

    public S_Chat(int type, ChatLog log) {
        writeC(Opcodes.S_CHAT);
        writeH(type);

        switch (type) {
        case TYPE_LOG: { // 이전 대화 목록
            writeD(log.getNo());
            writeD(log.getChatNo());
            writeD(log.getUserNo());
            writeS(log.getNickname());
            writeS(log.getMsg());
            writeD((int) (log.getRegdate().getTime() / 1000));
            break;
        }

        }
    }

    public S_Chat(int type, Chat chat) {
        writeC(Opcodes.S_CHAT);
        writeH(type);

        switch (type) {
        case TYPE_ADD: { // 채팅 목록
            writeD(chat.getNo());
            writeD((int) (chat.getRegdate().getTime() / 1000));

            List<User> memberList = chat.getMemberList();
            writeH(memberList.size());
            for (User member : memberList) {
                writeD(member.getNo());
                writeS(member.getId());
                writeS(member.getNickname());
                writeS(member.getStatusMsg());
                byte[] profile = member.getProfile();
                if (profile != null) {
                    writeH(profile.length);
                    writeBytes(profile);
                } else {
                    writeH(0);
                }
            }
            break;
        }

        }
    }

}
