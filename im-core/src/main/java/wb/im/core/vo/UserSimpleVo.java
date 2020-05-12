package wb.im.core.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSimpleVo {

    /**
     * 用户自增ID
     */
    private Integer userId;

    /**
     * 用户唯一ID
     */
    private Integer uniqueId;
}
