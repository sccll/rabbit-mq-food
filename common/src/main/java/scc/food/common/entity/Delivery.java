package scc.food.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author celine
 * @since 2021/8/4
 * 产品类
 */
@Builder
@Data
public class Delivery {

    /**
     * 送货员的身份证
     */
    @TableId(value = "delivery_id", type = IdType.AUTO)
    private Integer deliveryId;

    /**
     * 送货员的名字
     */
    private String deliveryName;

    /**
     * 区
     */
    private String district;

    /**
     * 状态
     */
    private String status;

    /**
     * 日期
     */
    private LocalDateTime date;
}
