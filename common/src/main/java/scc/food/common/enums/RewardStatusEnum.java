package scc.food.common.enums;

import lombok.Getter;

/**
 * @author Celine
 * @since 2021/08/04
 * 产品状态
 */
@Getter
public enum RewardStatusEnum {

    /**
     * 成功
     */
    SUCCESS(1, "成功"),

    /**
     * 失败
     */
    FAILED(2, "失败");

    private Integer id;

    private String name;

    RewardStatusEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
