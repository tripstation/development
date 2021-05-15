package com.manifesto.atm;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import com.manifesto.atm.entity.Atm;
import com.manifesto.atm.service.BankService;

@SpringBootApplication
public class AtmApplication extends SpringBootServletInitializer{
	
	@Autowired
	private BankService bankService;
	private static final Logger logger = LoggerFactory.getLogger(AtmApplication.class);
	
	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(AtmApplication.class);
		springApplication.setAddCommandLineProperties(false);
		springApplication.run(args);
	}
	
	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {

			List<String> atmList = bankService.parseTextFile();
			
			// one time operation to initialise the total cash in the atm
			String totalCash = atmList.get(0);
			logger.debug("totalCash = " + totalCash.trim());
			Atm atm = new Atm();
			atm.setTotalCash(Integer.parseInt(atmList.get(0)));
			bankService.saveAtm(atm);
			
			logger.debug("customer object to store = " + atmList.get(1));
			
			for(int i = 1; i < atmList.size(); i++) {
				String [] customerObjectArray = atmList.get(i).split("\\n");
				bankService.createNewCustomer(customerObjectArray);
			}
		};
	}

}