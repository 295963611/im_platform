package wb.im.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import wb.im.core.vo.MessageVo;
import wb.im.core.vo.UserSimpleVo;
import wb.im.domain.mapper.account.WbMessageInfoMapper;
import wb.im.domain.model.account.WbMessageInfo;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.sql.Wrapper;
import java.util.List;

@Slf4j
@Service
public class WbLoginTokenService implements IWbMessageService {

    @Resource
    private ChannelCache channelCache;

    @Resource
    private WbMessageAdapter wbMessageAdapter;

    @Resource
    WbMessageInfoMapper wbMessageInfoMapper;


    @Value("${im.token.key:}")
    private String aesKey;


    @Override
    public void messageOp(ChannelHandlerContext ctx, MessageDto messageDto, Integer isWeb) throws Exception {
        String token = messageDto.getToken();
        //解密token 主键ID +_+ 唯一ID
        UserSimpleVo userSimpleVo = getUserSimpleByToken(token, aesKey);
        log.info("用户已经加入聊天通道({})", JSON.toJSONString(userSimpleVo));
        //通道加入缓存
        channelCache.addChannel(userSimpleVo.getUserId(), ctx);

        MessageVo messageVo = MessageVo.SUCCESS(BusinessKeyEnums.TOKEN_LOGIN.getKey());
        //给予应答
        ctx.channel().writeAndFlush(JSON.toJSONString(messageVo));

        QueryWrapper<WbMessageInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("to_user_id", userSimpleVo.getUserId()).eq("status", StatusConstant.DEFAULT);
        List<WbMessageInfo> wbMessageInfos = wbMessageInfoMapper.selectList(queryWrapper);
        wbMessageInfos.forEach(v -> {
            log.info("用户（{}）得到离线消息({})", userSimpleVo.getUserId(), JSON.toJSONString(v));
            MessageInfoVo messageInfoVo = MessageInfoVo.builder().build();
            BeanUtils.copyProperties(v, messageInfoVo);
            ctx.channel().writeAndFlush(isWeb.equals(1) ? new TextWebSocketFrame(JSON.toJSONString(messageInfoVo)) : JSON.toJSONString(messageInfoVo));
        });


    }

    @Override
    @PostConstruct
    public void registerAdapter() {
        wbMessageAdapter.registerAdapter(BusinessKeyEnums.TOKEN_LOGIN, this);
    }
}
