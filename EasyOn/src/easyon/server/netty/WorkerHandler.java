package easyon.server.netty;

import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import easyon.config.Config;
import easyon.server.EasyClient;
import easyon.util.CommonUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;

/** Client�� Socket ó�� **/
public class WorkerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger _log = Logger.getLogger(WorkerHandler.class.getName());

    private AttributeKey<EasyClient> _state;

    // ä�� ���� ����
    private void close(ChannelHandlerContext ctx) {
        EasyClient client = ctx.attr(_state).get();
        if (client != null) {
            client.close();
        }
        ctx.close();
    }

    // ä�� ���(����) �̺�Ʈ
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        try {
            StringTokenizer st = new StringTokenizer(ctx.channel().remoteAddress().toString().substring(1), ":");
            String ip = st.nextToken();
            String port = st.nextToken();
            if (Config.WRITE_SOCKET_LOG) {
                CommonUtil.writeCommandLine("++ Client ���� : " + ip + ":" + port);
            }
        } catch (Exception e) {
            _log.log(Level.WARNING, "", e);
            close(ctx);
        }
    }

    // ä�� Ȱ�� �̺�Ʈ : ��� ���� ����
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        try {
            if (ctx.channel().isActive()) {
                EasyClient client = new EasyClient(ctx);
                _state = AttributeKey.valueOf(EasyClient.CLIENT_KEY);
                ctx.attr(_state).set(client);
                ctx.fireChannelRegistered();
            } else {
                close(ctx);
            }
        } catch (Exception e) {
            _log.log(Level.WARNING, "", e);
        }
    }

    // ��Ŷ ���� �̺�Ʈ
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            EasyClient client = ctx.attr(_state).get();

            if (client != null && client.getPacketData() != null) {
                // Ŭ���̾�Ʈ�� �������� ���� Ȯ��
                if (!client.isConnected()) {
                    close(ctx);
                    return;
                }

                ByteBuf in = (ByteBuf) msg;
                int size = in.readableBytes();
                if (size > 0 && size <= EasyClient.MAX_SIZE) {
                    byte[] data;
                    if (in.hasArray()) {
                        data = in.array();
                    } else {
                        data = new byte[size];
                        in.getBytes(in.readerIndex(), data);
                    }
                    System.arraycopy(data, 0, client.getPacketData(), client.getPacketIdx(), size);
                    client.addPacketIdx(size);
                } else {
                    client.close();
                }
            } else {
                close(ctx);
            }
        } catch (Exception e) {
            _log.log(Level.WARNING, "", e);
            close(ctx);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    // ��Ŷ ���� �Ϸ� �̺�Ʈ
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        try {
            ctx.flush();
        } catch (Exception e) {
            _log.log(Level.WARNING, "", e);
            close(ctx);
        }
    }

    // ���� ���� �̺�Ʈ
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        try {
            EasyClient client = ctx.attr(_state).get();
            if (client != null) {
                client.close();
            }
        } catch (Exception e) {
            _log.log(Level.WARNING, "", e);
            close(ctx);
        }
    }

    // ���� �߻� �̺�Ʈ: ���� ����
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        try {
        } catch (Exception e) {
            _log.log(Level.WARNING, "", e);
        } finally {
            close(ctx);
        }
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        // Not Use
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        // Not Use
    }

    // ��� �Ұ� ����
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // Not Use
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // Not Use
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        // Not Use
    }

}
