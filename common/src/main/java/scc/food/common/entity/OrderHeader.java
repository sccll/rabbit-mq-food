package scc.food.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author celine
 * @since 2021/8/3
 */
@Builder
@Data
public class OrderHeader {

    /**
     * 订单id
     */
    @TableId(value = "order_id", type = IdType.AUTO)
    private Integer orderId;

    /**
     * 订单状态
     */
    private String status;

    /**
     * 地址
     */
    private String address;

    /**
     * 帐户id
     */
    private Integer accountId;

    /**
     * 产品id
     */
    private Integer productId;

    /**
     * 骑手id
     */
    private Integer deliveryId;

    /**
     * 结算id
     */
    private Integer settlementId;

    /**
     * 价格， 钱不能使用double和float，有精度
     */
    private BigDecimal price;

    /**
     * 积分id
     */
    private Integer rewardId;

    /**
     * 日期
     */
    private LocalDateTime date;
}
