package com.manifesto.atm;

import java.util.Arrays;
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
import com.manifesto.atm.model.AtmRepository;
import com.manifesto.atm.service.BankService;

@SpringBootApplication
public class AtmApplication extends SpringBootServletInitializer{
	
	@Autowired
	private BankService bankService;
	@Autowired
	private AtmRepository atmRepository;
	private static final Logger logger = LoggerFactory.getLogger(AtmApplication.class);
	
	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(AtmApplication.class);
		springApplication.setAddCommandLineProperties(false);
		springApplication.run(args);
	}
	
	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {

//			System.out.println("Let's inspect the args passed in:");
//			for(String arg : args) {
//				System.out.println(arg);
//			}
//			String[] beanNames = ctx.getBeanDefinitionNames();
//			Arrays.sort(beanNames);
//			for (String beanName : beanNames) {
//				System.out.println(beanName);
//			}
			List<String> atmList = bankService.parseTextFile();
			for(String line : atmList) {
//				logger.debug(line);
			}
			// one time operation to initialise the total cash in the atm
			String totalCash = atmList.get(0);
			logger.debug("totalCash = " + totalCash.trim());
			Atm atm = new Atm();
			atm.setTotalCash(Integer.parseInt(atmList.get(0)));
			bankService.saveAtm(atm);
			
			
			logger.debug("customer object to store = " + atmList.get(1));
//			String [] customerObjectArray = atmList.get(1).split("\\n");
//			bankService.createNewCustomer(customerObjectArray);
			
			for(int i = 1; i < atmList.size(); i++) {
				String [] customerObjectArray = atmList.get(i).split("\\n");
//				logger.debug("customer object = " + customerObjectArray[i]);
				bankService.createNewCustomer(customerObjectArray);
			}
//			for(String element: customerObjectArray) {
//				logger.debug("element = " + element);
				//call customer save with [0] (and check pin is correct)
				// and [1] to add the balance and overdraft
				//then [2] is a list of operations
//			}
//			for(String line : atmList) {
//				logger.debug(line);	
//				if(line.equals("operation")) {
//					continue;
//				}
//				logger.debug("got " + line);
//			}

		};
	}

}
