package scc.food.delivery;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Celine
 * @since 2021/08/04
 */
@MapperScan("scc.food.delivery.mapper")
@SpringBootApplication
public class ServiceDeliveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceDeliveryApplication.class, args);
	}

}
