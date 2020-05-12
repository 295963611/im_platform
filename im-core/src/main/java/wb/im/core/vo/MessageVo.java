package wb.im.core.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageVo {

    public static MessageVo SUCCESS(Integer businessKey) {
        return MessageVo.builder().code(200).businessKey(businessKey).msgType(1).message("SUCCESS").build();
    }

    /**
     * 业务CODE
     */
    private Integer code;

    /**
     * 业务KEY
     */
    private Integer businessKey;

    /**
     * 消息类型
     */
    private Integer msgType;

    /**
     * 业务说明
     */
    private String message;

    /**
     * 业务数据
     */
    private Object data;
}
