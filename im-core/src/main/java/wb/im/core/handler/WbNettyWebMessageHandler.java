package wb.im.core.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class WbNettyWebMessageHandler extends ChannelInitializer<SocketChannel> {

    @Resource
    WbNettyMessageProcessorHandler wbNettyMessageProcessorHandler;

    @Resource
    WbNettyActiveHandler wbNettyActiveHandler;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        //用户定义的ChannelInitailizer加入到这个channel的pipeline上面去，这个handler就可以用于处理当前这个channel上面的一些事件
        ChannelPipeline pipeline = socketChannel.pipeline();
        //ChannelPipeline类似于一个管道，管道中存放的是一系列对读取数据进行业务操作的ChannelHandler。

        /**
         * 发送的数据在管道里是无缝流动的，在数据量很大时，为了分割数据，采用以下几种方法
         * LineBasedFrameDecoder 时间解码器
         * DelimiterBasedFrameDecoder 分隔符解码器
         * FixedLengthFrameDecoder 定长解码器
         */
        //设置连接符/分隔符，换行显示

        //websocket协议本身是基于http协议的，所以这边也要使用http解编码器
        pipeline.addLast("http-codec", new HttpServerCodec());//设置解码器
        pipeline.addLast("aggregator", new HttpObjectAggregator(65536));//聚合器，使用websocket会用到
        pipeline.addLast("http-chunked", new ChunkedWriteHandler());//用于大数据的分区传输
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws", "WebSocket", true, 65536 * 10));
        pipeline.addLast("handler", wbNettyMessageProcessorHandler);
    }
}
