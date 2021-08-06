package scc.food.restaurant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import scc.food.common.entity.Restaurant;
import scc.food.restaurant.mapper.RestaurantMapper;
import scc.food.restaurant.service.RestaurantService;

/**
 * @author celine
 * @since 2021/8/4
 */
@Service
public class RestaurantServiceImpl extends ServiceImpl<RestaurantMapper, Restaurant> implements RestaurantService {
}
