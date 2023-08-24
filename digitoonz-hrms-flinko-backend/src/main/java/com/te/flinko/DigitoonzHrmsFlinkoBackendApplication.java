package com.te.flinko;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.te.flinko.*")
@ComponentScan(basePackages = { "com.te.flinko.*" })
@EntityScan("com.te.flinko.*")
public class DigitoonzHrmsFlinkoBackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(DigitoonzHrmsFlinkoBackendApplication.class, args);
	}
}
