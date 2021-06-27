package com.floow.insurance;

import java.io.FileOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import com.floow.insurance.model.Driver;
import com.floow.insurance.service.DriverService;

@SpringBootApplication
public class InsuranceServiceApplication {

	@Autowired
	DriverService driverService;
	
	public static void main(String[] args) {
		SpringApplication.run(InsuranceServiceApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			new FileOutputStream("insurance.csv", true).close();
			Driver james = new Driver("James","Smith",driverService.stringToDate("1970-01-01"));
			Driver sally = new Driver("Sally","Brown",driverService.stringToDate("1999-05-01"));
			Driver jimi = new Driver("Jimi","Hendrix",driverService.stringToDate("1942-11-27"));
			
			driverService.saveDriver(james);
			driverService.saveDriver(sally);
			driverService.saveDriver(jimi);
		};
	}
}