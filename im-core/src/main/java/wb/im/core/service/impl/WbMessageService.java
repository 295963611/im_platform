package wb.im.core.service.impl;

import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import wb.im.core.adapter.WbMessageAdapter;
import wb.im.core.cache.ChannelCache;
import wb.im.core.constant.BusinessKeyEnums;
import wb.im.core.constant.StatusConstant;
import wb.im.core.dto.MessageDto;
import wb.im.core.service.IWbMessageService;
import wb.im.core.vo.MessageInfoVo;
import wb.im.core.vo.UserSimpleVo;
import wb.im.domain.mapper.account.WbMessageInfoMapper;
import wb.im.domain.model.account.WbMessageInfo;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@Service
public class WbMessageService implements IWbMessageService {

    @Resource
    WbMessageAdapter wbMessageAdapter;

    @Resource
    WbMessageInfoMapper wbMessageInfoMapper;

    @Resource
    ChannelCache channelCache;

    @Value("${im.token.key:}")
    private String aesKey;

    @Override
    public void messageOp(ChannelHandlerContext ctx, MessageDto messageDto, Integer isWeb) throws Exception {
        String token = messageDto.getToken();
        //解密token 主键ID +_+ 唯一ID
        UserSimpleVo userSimpleVo = getUserSimpleByToken(token, aesKey);
        WbMessageInfo wbMessageInfo = WbMessageInfo.builder()
                .fromUserId(userSimpleVo.getUserId())
                .toUserId(messageDto.getToUserId())
                .businessType(messageDto.getBusinessKey().shortValue())
                .msgType(messageDto.getMsgType().shortValue())
                .status(StatusConstant.DEFAULT)
                .addDate(new Date())
                .content(messageDto.getContent())
                .build();
        wbMessageInfoMapper.insert(wbMessageInfo);
        Channel channel = channelCache.getChannelById(messageDto.getToUserId());

        if (channel == null) {
            log.info("({})用户不在线", messageDto.getToUserId());
            return;
        }

        MessageInfoVo messageInfoVo = MessageInfoVo.builder().build();
        BeanUtils.copyProperties(wbMessageInfo, messageInfoVo);
        channel.writeAndFlush(isWeb.equals(1) ? new TextWebSocketFrame(JSON.toJSONString(messageInfoVo)) : JSON.toJSONString(messageInfoVo));
        wbMessageInfoMapper.updateById(WbMessageInfo.builder().id(wbMessageInfo.getId()).status(StatusConstant.ALREADY).build());
    }

    @Override
    @PostConstruct
    public void registerAdapter() {
        wbMessageAdapter.registerAdapter(BusinessKeyEnums.CHAT_MESSAGE, this);
    }
}
