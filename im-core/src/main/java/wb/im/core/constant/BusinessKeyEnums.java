package wb.im.core.constant;

import lombok.Getter;
import lombok.Setter;

/**
 * 消息类型枚举
 */
public enum BusinessKeyEnums {

    TOKEN_LOGIN(10000, "用户登陆TOKEN"),
    CHAT_MESSAGE(20000, "聊天消息"),
    HEART_BEAT(0, "心跳"),
    ;


    @Getter
    @Setter
    private Integer key;

    @Getter
    @Setter
    private String memo;

    BusinessKeyEnums(Integer key, String memo) {
        this.key = key;
        this.memo = memo;
    }
}
