package wb.im.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import wb.im.core.adapter.WbMessageAdapter;
import wb.im.core.constant.BusinessKeyEnums;
import wb.im.core.dto.MessageDto;
import wb.im.core.service.IWbMessageService;
import wb.im.core.vo.MessageInfoVo;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@Service
public class WbHeartBeatService implements IWbMessageService {

    @Resource
    private WbMessageAdapter wbMessageAdapter;

    @Override
    public void messageOp(ChannelHandlerContext ctx, MessageDto messageDto, Integer isWeb) throws Exception {
        log.info("收到心跳({})", JSON.toJSONString(messageDto));
        String jsonString = JSON.toJSONString(MessageInfoVo.builder()
                .content("pong").addDate(new Date())
                .businessType(BusinessKeyEnums.HEART_BEAT.getKey().shortValue())
                .msgType((short) 1).build());
        ctx.channel().writeAndFlush(isWeb.equals(1) ? new TextWebSocketFrame(jsonString) : jsonString);
    }

    @Override
    @PostConstruct
    public void registerAdapter() {
        wbMessageAdapter.registerAdapter(BusinessKeyEnums.HEART_BEAT, this);
    }
}
