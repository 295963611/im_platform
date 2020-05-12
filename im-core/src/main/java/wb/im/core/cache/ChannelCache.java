package wb.im.core.cache;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class ChannelCache {

    //全局用户channel 组
    private ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    //用户channel对应关系 后期可使用redis 做分布式
    private ConcurrentHashMap<Integer, Channel> channelMap = new ConcurrentHashMap<>();

    //存用户与channel反向关系 后期可使用redis 做分布式
    private ConcurrentHashMap<String, Integer> channelUserMap = new ConcurrentHashMap<>();

    /**
     * 得到用户组
     *
     * @return
     */
    public ChannelGroup getChannelGroup() {
        return channelGroup;
    }

    /**
     * 得到用户组
     *
     * @return
     */
    public ConcurrentHashMap<Integer, Channel> getChannelMap() {
        return channelMap;
    }

    /**
     * 根据用户ID 得到对应的channel
     *
     * @param userId
     * @return
     */
    public Channel getChannelById(Integer userId) {

        return getChannelByChannleId(channelMap.get(userId).id());
    }

    /**
     * 根据channelId得到用户信息
     *
     * @param channelId
     * @return
     */
    public Channel getChannelByChannleId(ChannelId channelId) {
        return channelGroup.find(channelId);
    }

    /**
     * 添加用户信息
     *
     * @param userId
     * @param channelHandlerContext
     */
    public void addChannel(Integer userId, ChannelHandlerContext channelHandlerContext) {
        log.info("用户加入({})", userId);
        channelGroup.add(channelHandlerContext.channel());
        channelMap.put(userId, channelHandlerContext.channel());
        channelUserMap.put(channelHandlerContext.channel().id().asLongText(), userId);
    }

    /**
     * 删除对应通道
     *
     * @param channelHandlerContext
     */
    public void removeChannel(ChannelHandlerContext channelHandlerContext) {
        String channelId = channelHandlerContext.channel().id().asLongText();
        Integer userId = channelUserMap.get(channelId);
        log.info("用户断开({})", userId);
        channelGroup.remove(channelHandlerContext.channel());
        if (userId != null) {
            channelMap.remove(userId);
            channelUserMap.remove(channelId);
        }


    }
}
