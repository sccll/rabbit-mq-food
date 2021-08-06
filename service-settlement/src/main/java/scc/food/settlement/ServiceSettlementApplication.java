package scc.food.settlement;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Celine
 * @since 2021/08/04
 */
@MapperScan("scc.food.settlement.mapper")
@SpringBootApplication
public class ServiceSettlementApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceSettlementApplication.class, args);
	}

}
