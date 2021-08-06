package scc.food.common.enums;

import lombok.Getter;

/**
 * @author Celine
 * @since 2021/08/04
 * 产品状态
 */
@Getter
public enum ProductStatusEnum {

    /**
     * 可售卖
     */
    AVAILABLE(1, "可售卖"),

    /**
     * 不可售卖
     */
    NOT_AVAILABLE(2, "不可售卖");

    private Integer id;

    private String name;

    ProductStatusEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
