package scc.food.settlement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import scc.food.common.entity.Settlement;

/**
 * @author celine
 * @since 2021/8/4
 */
public interface SettlementService extends IService<Settlement>  {

    /**
     * 插入结算
     *
     * @param settlement 结算
     */
    void insertSettlement(Settlement settlement);
}
