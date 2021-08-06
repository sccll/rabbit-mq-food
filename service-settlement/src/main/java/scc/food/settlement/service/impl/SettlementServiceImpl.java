package scc.food.settlement.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import scc.food.common.entity.Settlement;
import scc.food.settlement.mapper.SettlementMapper;
import scc.food.settlement.service.SettlementService;

/**
 * @author celine
 * @since 2021/8/4
 */
@Service
public class SettlementServiceImpl extends ServiceImpl<SettlementMapper, Settlement> implements SettlementService {

    @Override
    public void insertSettlement(Settlement settlement) {
        baseMapper.insert(settlement);
    }
}
