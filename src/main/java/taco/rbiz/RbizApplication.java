package taco.rbiz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RbizApplication {

	public static void main(String[] args) {
		SpringApplication.run(RbizApplication.class, args);
	}

}
