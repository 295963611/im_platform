package wb.im.domain.model.account;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "wb_message_info")
public class WbMessageInfo implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 发送人
     */
    @TableField(value = "from_user_id")
    private Integer fromUserId;

    /**
     * 接收人
     */
    @TableField(value = "to_user_id")
    private Integer toUserId;

    /**
     * 业务类型
     */
    @TableField(value = "business_type")
    private Short businessType;

    /**
     * 消息类型
     */
    @TableField(value = "msg_type")
    private Short msgType;

    /**
     * 消息状态
     */
    private Short status;

    /**
     * 发送时间
     */
    @TableField(value = "add_date")
    private Date addDate;

    /**
     * 阅读时间
     */
    @TableField(value = "read_date")
    private Date readDate;

    /**
     * 修改时间
     */
    @TableField(value = "update_date")
    private Date updateDate;

    /**
     * 消息内容
     */
    private String content;

    private static final long serialVersionUID = 1L;
}