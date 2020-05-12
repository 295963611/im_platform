package wb.im.core.adapter;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import wb.im.core.constant.BusinessKeyEnums;
import wb.im.core.dto.MessageDto;
import wb.im.core.service.IWbMessageService;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class WbMessageAdapter {

    /**
     * 业务处理器
     */
    private ConcurrentHashMap<Integer, IWbMessageService> messageServiceMap = new ConcurrentHashMap<>();

    /**
     * 消息处理
     *
     * @param ctx
     * @param msg
     */
    public void acceptMessage(ChannelHandlerContext ctx, Object msg) {

        String message = null;
        //web消息
        if (msg instanceof WebSocketFrame) {
            message = ((TextWebSocketFrame) msg).text();
        } else {
            //socket 消息
            ByteBuf buff = (ByteBuf) msg;
            message = buff.toString(CharsetUtil.UTF_8).trim();
        }

        if (StringUtils.isBlank(message)) {
            log.info("收到消息为空");
            return;
        }
        MessageDto messageDto = JSON.parseObject(message, MessageDto.class);
        try {
            messageServiceMap.get(messageDto.getBusinessKey()).messageOp(ctx, messageDto, msg instanceof WebSocketFrame ? 1 : 0);
        } catch (Exception e) {
            log.error("适配器转发信息出错");
            e.printStackTrace();
        }

    }

    /**
     * 注册适配器
     *
     * @param businessKeyEnums
     * @param iWbMessageService
     */
    public void registerAdapter(BusinessKeyEnums businessKeyEnums, IWbMessageService iWbMessageService) {
        messageServiceMap.put(businessKeyEnums.getKey(), iWbMessageService);
    }
}
