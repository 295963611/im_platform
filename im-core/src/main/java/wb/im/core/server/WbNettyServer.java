package wb.im.core.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import wb.im.core.handler.WbNettyMessageHandler;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

@Slf4j
@Component
public class WbNettyServer implements Runnable {

    @Value("${im.server.port:}")
    private Integer serverPort;

    @Resource
    WbNettyMessageHandler wbNettyMessageHandler;

    EventLoopGroup mainGroup = null;

    EventLoopGroup workerGroup = null;

    @PostConstruct
    public void start() {
        log.info("消息服务开始启动（{}）", serverPort);
        Thread server = new Thread(this);
        server.start();
    }

    @PreDestroy
    public void destroy() {
        log.info("消息服务开始关闭({})", serverPort);
        if (mainGroup != null) {
            mainGroup.shutdownGracefully();
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
    }

    @Override
    public void run() {

        /**
         * 配置服务端的NIO线程组
         * NioEventLoopGroup 是用来处理I/O操作的Reactor线程组
         * bossGroup：用来接收进来的连接，workerGroup：用来处理已经被接收的连接,进行socketChannel的网络读写，
         * bossGroup接收到连接后就会把连接信息注册到workerGroup
         * workerGroup的EventLoopGroup默认的线程数是CPU核数的二倍
         */
        mainGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();
        /**
         * ServerBootstrap 是一个启动NIO服务的辅助启动类
         */
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        /**
         * ServerSocketChannel是以NIO的selector为基础进行实现的，用来接收新的连接，这里告诉Channel通过NioServerSocketChannel获取新的连接
         * option是设置 bossGroup，childOption是设置workerGroup
         * netty 默认数据包传输大小为1024字节, 设置它可以自动调整下一次缓冲区建立时分配的空间大小，避免内存的浪费    最小  初始化  最大 (根据生产环境实际情况来定)
         * 使用对象池，重用缓冲区

         */
        serverBootstrap.group(mainGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator(64, 10496, 1048576))
                .childOption(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator(64, 10496, 1048576))
                .childHandler(wbNettyMessageHandler)
        ;

        try {
            ChannelFuture future = serverBootstrap.bind(serverPort).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //关闭主线程组
            mainGroup.shutdownGracefully();
            //关闭工作线程组
            workerGroup.shutdownGracefully();
        }
    }
}
