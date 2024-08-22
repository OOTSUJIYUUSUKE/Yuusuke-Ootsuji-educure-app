package jp.co.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "jp.co.example.entity")
public class YuusukeOotsujiEducureAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(YuusukeOotsujiEducureAppApplication.class, args);
	}

}
