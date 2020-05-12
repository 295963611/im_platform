package wb.im.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {

    /**
     * 登陆者token token组装 用户表自增ID_唯一ID
     */
    private String token;

    /**
     * 业务类型
     */
    private Integer businessKey;

    /**
     * 消息类型 1、文字消息
     */
    @Builder.Default
    private Integer msgType = 1;

    /**
     * 业务内容
     */
    private String content;

    /**
     * 收消息人
     */
    private Integer toUserId;

    /**
     * 收消息房间号
     */
    private Integer toRoomId;
}
