package scc.food.reward.service;

import com.baomidou.mybatisplus.extension.service.IService;
import scc.food.common.entity.Reward;

/**
 * @author celine
 * @since 2021/8/4
 */
public interface RewardService extends IService<Reward>  {

    /**
     * 插入奖励
     *
     * @param reward 奖励
     */
    void insertReward(Reward reward);
}
