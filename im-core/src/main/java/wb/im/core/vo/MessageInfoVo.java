package wb.im.core.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageInfoVo {


    private Integer id;

    /**
     * 发送人
     */
    private Integer fromUserId;

    /**
     * 接收人
     */
    private Integer toUserId;

    /**
     * 业务类型
     */
    private Short businessType;

    /**
     * 消息类型
     */
    private Short msgType;

    /**
     * 发送时间
     */
    private Date addDate;

    /**
     * 消息内容
     */
    private String content;
}
