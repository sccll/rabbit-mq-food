package scc.food.restaurant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import scc.food.common.entity.Product;
import scc.food.restaurant.mapper.ProductMapper;
import scc.food.restaurant.service.ProductService;

/**
 * @author celine
 * @since 2021/8/4
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
}
