package wb.im.domain.model.account;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "wb_account_info")
public class WbAccountInfo implements Serializable {
    /**
     * 自增ID
     */
    @TableId(value = "id")
    private Integer id;

    /**
     * 用户唯一标识
     */
    @TableField(value = "user_id")
    private Integer userId;

    /**
     * 头像
     */
    @TableField(value = "head_pic")
    private String headPic;

    /**
     * 性别
     */
    private Short sex;

    /**
     * 注册生成TOKEN
     */
    private String token;

    /**
     * 状态
     */
    private Short status;

    /**
     * 添加时间
     */
    @TableField(value = "add_date")
    private Date addDate;

    /**
     * 修改时间
     */
    @TableField(value = "update_date")
    private Date updateDate;

    private static final long serialVersionUID = 1L;
}