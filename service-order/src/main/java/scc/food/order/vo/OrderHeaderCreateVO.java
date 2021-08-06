package scc.food.order.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author celine
 * @since 2021/8/3
 */
@Builder
@Data
public class OrderHeaderCreateVO {

    /**
     * 帐户id
     */
    private Integer accountId;

    /**
     * 地址
     */
    private String address;

    /**
     * 产品id
     */
    private Integer productId;
}
