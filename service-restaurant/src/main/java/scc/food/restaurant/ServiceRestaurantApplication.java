package scc.food.restaurant;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Celine
 * @since 2021/08/04
 */
@MapperScan("scc.food.restaurant.mapper")
@SpringBootApplication
public class ServiceRestaurantApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceRestaurantApplication.class, args);
	}

}
