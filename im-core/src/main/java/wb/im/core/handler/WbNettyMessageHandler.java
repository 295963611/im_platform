package wb.im.core.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * IO处理通道初始化
 */
@Component
public class WbNettyMessageHandler extends ChannelInitializer<Channel> {

    @Resource
    WbNettyMessageProcessorHandler wbNettyMessageProcessorHandler;

    @Resource
    WbNettyActiveHandler wbNettyActiveHandler;

    @Override
    protected void initChannel(Channel channel) throws Exception {
        channel.pipeline().addLast("encoder", new StringEncoder());

        channel.pipeline().addLast("decoder", new StringDecoder());

        channel.pipeline().addLast("activeHandler", wbNettyActiveHandler);
        /**
         * 自定义ChannelInboundHandlerAdapter
         */
        channel.pipeline().addLast(wbNettyMessageProcessorHandler);

    }
}
