package scc.food.common.enums;

import lombok.Getter;

/**
 * @author celine
 * @since 2021/8/3
 */
@Getter
public enum OrderHeaderStatusEnum {

    /**
     * 创建中
     */
    ORDER_CREATING(1, "ORDER_CREATING"),

    /**
     * 餐厅已确认
     */
    RESTAURANT_CONFIRMED(2, "RESTAURANT_CONFIRMED"),

    /**
     * 骑手已确认
     */
    DELIVERY_CONFIRMED(3, "DELIVERY_CONFIRMED"),

    /**
     * 已结算
     */
    SETTLEMENT_CONFIRMED(4, "SETTLEMENT_CONFIRMED"),

    /**
     * 订单创建完成
     */
    ORDER_CREATED(5, "ORDER_CREATED"),

    /**
     * 订单创建失败
     */
    ORDER_FAILED(6, "ORDER_FAILED");

    private Integer id;

    private String name;

    OrderHeaderStatusEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
