package com.manifesto.atm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
public class AtmApplication {
	
	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(AtmApplication.class);
		springApplication.run(args);
	}
}