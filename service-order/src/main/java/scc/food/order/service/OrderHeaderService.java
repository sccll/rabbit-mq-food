package scc.food.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import scc.food.common.entity.OrderHeader;
import scc.food.order.vo.OrderHeaderCreateVO;

/**
 * @author celine
 * @since 2021/8/3
 * 处理用户关于订单的请求
 */
public interface OrderHeaderService extends IService<OrderHeader> {

    /**
     * 创建订单
     *
     * @param orderVo 订单信息
     */
    void createOrderHeader(OrderHeaderCreateVO orderVo);

}
