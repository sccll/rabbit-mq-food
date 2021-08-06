package scc.food.common.enums;

import lombok.Getter;

/**
 * @author Celine
 * @since 2021/08/04
 * 产品状态
 */
@Getter
public enum DeliveryStatusEnum {

    /**
     * 可配送
     */
    AVAILABLE(1, "可配送"),

    /**
     * 不可配送
     */
    NOT_AVAILABLE(2, "不可配送");

    private Integer id;

    private String name;

    DeliveryStatusEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
