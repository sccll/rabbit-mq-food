package scc.food.common.enums;

import lombok.Getter;

/**
 * @author Celine
 * @since 2021/08/04
 * 产品状态
 */
@Getter
public enum SettlementStatusEnum {

    /**
     * 结算成功
     */
    SUCCESS(1, "结算成功"),

    /**
     * 结算失败
     */
    FAILED(2, "结算失败");

    private Integer id;

    private String name;

    SettlementStatusEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
