package scc.food.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author celine
 * @since 2021/8/4
 * 餐厅类
 */
@Builder
@Data
public class Restaurant {

    /**
     * 餐厅id
     */
    @TableId(value = "restaurant_id", type = IdType.AUTO)
    private Integer restaurantId;

    /**
     * 餐厅的名字
     */
    private String restaurantName;

    /**
     * 地址
     */
    private String address;

    /**
     * 状态
     */
    private String status;

    /**
     * 结算id
     */
    private Integer settlementId;

    /**
     * 日期
     */
    private LocalDateTime date;
}
