package wb.im.core.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import wb.im.core.adapter.WbMessageAdapter;

import javax.annotation.Resource;

/**
 * 自定义消息处理器
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class WbNettyMessageProcessorHandler extends ChannelInboundHandlerAdapter {

    @Resource
    WbMessageAdapter wbMessageAdapter;


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        wbMessageAdapter.acceptMessage(ctx, msg);
    }
}
