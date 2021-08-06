package scc.food.common.enums;

import lombok.Getter;

/**
 * @author Celine
 * @since 2021/08/04
 * 餐厅状态
 */
@Getter
public enum RestaurantStatusEnum {

    /**
     * 营业中
     */
    OPEN(1, "营业中"),

    /**
     * 关闭
     */
    CLOSE(2, "关门");

    private Integer id;

    private String name;

    RestaurantStatusEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
