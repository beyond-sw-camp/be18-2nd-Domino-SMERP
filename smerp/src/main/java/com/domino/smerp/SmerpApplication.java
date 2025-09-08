package com.domino.smerp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SmerpApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmerpApplication.class, args);
	}

}
