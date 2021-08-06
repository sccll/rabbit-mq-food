package scc.food.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author celine
 * @since 2021/8/3
 * 对外通讯的数据，不要使用基础数据类型
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderMessageDTO {

    /**
     * 订单id
     */
    private Integer orderId;

    /**
     * 订单状态
     */
    private Integer orderStatus;

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
     * 积分奖励数量
     */
    private Integer rewardAmount;

    /**
     * 确认
     */
    private Boolean confirmed;

}
