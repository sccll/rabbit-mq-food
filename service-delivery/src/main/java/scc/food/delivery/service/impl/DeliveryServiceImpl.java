package scc.food.delivery.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import scc.food.common.entity.Delivery;
import scc.food.delivery.mapper.DeliveryMapper;
import scc.food.delivery.service.DeliveryService;

/**
 * @author celine
 * @since 2021/8/4
 */
@Service
public class DeliveryServiceImpl extends ServiceImpl<DeliveryMapper, Delivery> implements DeliveryService {
}
