package scc.food.reward.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import scc.food.common.entity.Reward;
import scc.food.reward.mapper.RewardMapper;
import scc.food.reward.service.RewardService;

/**
 * @author celine
 * @since 2021/8/4
 */
@Service
public class RewardServiceImpl extends ServiceImpl<RewardMapper, Reward> implements RewardService {

    @Override
    public void insertReward(Reward reward) {
        baseMapper.insert(reward);
    }
}
