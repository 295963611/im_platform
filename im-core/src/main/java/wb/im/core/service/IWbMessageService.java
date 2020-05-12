package wb.im.core.service;

import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang3.StringUtils;
import wb.im.core.dto.MessageDto;
import wb.im.core.vo.UserSimpleVo;
import wb.im.utils.encrypt.AESUtil;

import javax.annotation.PostConstruct;

public interface IWbMessageService {

    /**
     * 消息处理
     *
     * @param ctx
     * @param messageDto
     * @throws Exception
     */
    void messageOp(ChannelHandlerContext ctx, MessageDto messageDto, Integer isWeb) throws Exception;

    /**
     * 适配器注册
     */
    void registerAdapter();

    default UserSimpleVo getUserSimpleByToken(String token, String aesKey) throws Exception {
        if (StringUtils.isBlank(token)) {
            return null;
        }
        String deToken = AESUtil.decode(aesKey, token);
        if (StringUtils.isBlank(deToken)) {
            return null;
        }
        String[] userIds = deToken.split("_");

        return UserSimpleVo.builder().userId(Integer.valueOf(userIds[0])).uniqueId(Integer.valueOf(userIds[1])).build();
    }
}
