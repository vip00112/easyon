package easyon.server.netty;

import java.util.logging.Level;
import java.util.logging.Logger;

import easyon.config.Config;
import easyon.util.GeneralThreadPool;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/** Socket Server **/
public class Server implements Runnable {
    private static final Logger _log = Logger.getLogger(Server.class.getName());

    private static final Server instance = new Server();

    public static Server getInstance() {
        return instance;
    }

    /** netty socket server start **/
    public void start() {
        GeneralThreadPool.getInstance().execute(this);
    }

    @Override
    public void run() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new WorkerHandler()); // Packet �ۼ��� Handler ���
                        }
                    })
                    .childOption(ChannelOption.TCP_NODELAY, true);

            // Bind
            ChannelFuture cf = b.bind(Config.PORT).sync();
            System.out.println("+++++ ���� ���� �Ϸ� : " + Config.PORT + " ��Ʈ +++++");
            System.out.println("");

            // Socket Close���� ���
            cf.channel().closeFuture().sync();
        } catch (Exception e) {
            _log.log(Level.WARNING, "", e);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

}
