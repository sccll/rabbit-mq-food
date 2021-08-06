package scc.food.reward;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @author Celine
 * @since 2021/08/04
 */
@MapperScan("scc.food.reward.mapper")
@SpringBootApplication
public class ServiceRewardApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceRewardApplication.class, args);
	}

}
