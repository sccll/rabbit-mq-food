package scc.food.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author celine
 * @since 2021/8/4
 * 产品类
 */
@Builder
@Data
public class Reward {

    /**
     * 奖励id
     */
    @TableId(value = "reward_id", type = IdType.AUTO)
    private Integer rewardId;


    /**
     * 订单id
     */
    private Integer orderId;

    /**
     * 价格
     */
    private BigDecimal amount;

    /**
     * 状态
     */
    private String status;

    /**
     * 日期
     */
    private LocalDateTime date;
}
