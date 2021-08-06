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
public class Product {

    /**
     * 产品id
     */
    @TableId(value = "product_id", type = IdType.AUTO)
    private Integer productId;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 餐厅id
     */
    private Integer restaurantId;

    /**
     * 状态
     */
    private String status;

    /**
     * 日期
     */
    private LocalDateTime date;
}
