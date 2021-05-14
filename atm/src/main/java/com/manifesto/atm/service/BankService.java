package com.manifesto.atm.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.manifesto.atm.entity.Atm;
import com.manifesto.atm.entity.Customer;
import com.manifesto.atm.model.AtmRepository;
import com.manifesto.atm.model.CustomerRepository;


@Transactional
@Service
public class BankService {

	@Autowired
	AtmRepository atmRepository;
	
	@Autowired
	CustomerRepository customerRepository;

	private static final Logger logger = LoggerFactory.getLogger(BankService.class);
	private static final String ACCOUNT_ERR = "ACCOUNT_ERR";
	private static final String FUNDS_ERR = "FUNDS_ERR";
	private static final String ATM_ERR = "ATM_ERR";
	public Customer saveCustomer(Customer customer) {
		return customerRepository.save(customer);
	}
	
	public Atm saveAtm(Atm atm) {
		return atmRepository.save(atm);
	}
	
	// why are we doing this?
	public Optional<Atm> findCustomerById(Long id) {
		return atmRepository.findById(id);
	}


	public Optional<Atm> findAtmById(Long id) {
		return atmRepository.findById(id);
	}

	public Optional<Customer> findCustomerByAccountNumber(int account) {
		return customerRepository.findByAccountNumber(account);
	}

	public List<String> parseTextFile() {
		logger.debug("in parseTextFile()");
		List<String> atmList = new ArrayList<>();
		String inputFile = "atm.txt";
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(inputFile).getFile());
		String atmFile = new String();
		
		try {
			atmFile = FileUtils.readFileToString(file,"UTF-8");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		//split the string by blank lines
		String [] tokens = atmFile.split("\n\\s*\n");
		for(String token : tokens) {
//			logger.debug("we have split the file here " + token);
			atmList.add(token.trim());
		}
		return(atmList);
	}

	public void createNewCustomer(String[] inputOperationArray) {
		logger.debug("customerObjectArray element [0] = " + inputOperationArray[0]);
		String [] urlArray = inputOperationArray[0].split(" "); //todo: handle new line
		logger.debug("got arg 1 as " + urlArray[0]); // atm total cash
		logger.debug("got arg 2 as " + urlArray[1]); // account number
		logger.debug("got arg 3 as " + urlArray[2]); // correct pin
		int accountNumber = Integer.parseInt(urlArray[0].trim());
		int correctPin = Integer.parseInt(urlArray[1].trim());
		int enteredPin = Integer.parseInt(urlArray[2].trim());
		//initialise the Atm if we don't already have one
//		Optional<Atm> atm = findAtmById(1L);
		//first check pin
		if(enteredPin == correctPin) {
			logger.debug("pins match");
			Optional<Customer> customer = findCustomerByAccountNumber(accountNumber);
			if(customer.isPresent()) {
				Customer aCustomer = customer.get();
				for(int i = 2;i < inputOperationArray.length; i ++) {
					logger.debug("inputOperationArray element [i] = " + inputOperationArray[i]);
					if ( inputOperationArray[i].startsWith("B")) {
						logger.debug("got a B operation doing a balance");
						logger.debug("Balance = " + aCustomer.getBalance());
						
					}else if (inputOperationArray[i].startsWith("W")) {
						logger.debug("got a W operation doing a withdrawal");
						String [] amountArray = inputOperationArray[i].split(" ");
						int withdrawalAmount = Integer.parseInt(amountArray[1].trim());
						logger.debug("got amount as " + withdrawalAmount);
						if(aCustomer.getAtm().getTotalCash() - withdrawalAmount > 0) {
							int balance =  (aCustomer.isOverDraftActive()) ?   aCustomer.getBalance() :  aCustomer.getBalance() + aCustomer.getOverdraftFacility();
							logger.debug("we just set balance to " + balance);
							logger.debug("is overdraft active " + aCustomer.isOverDraftActive());
							if(withdrawalAmount < balance) {
								logger.debug("we will still be in credit");
								aCustomer.setBalance(balance - withdrawalAmount);
								//decrement atm cash total by withdrawal amount
								aCustomer.getAtm().setTotalCash(aCustomer.getAtm().getTotalCash() - withdrawalAmount);
								logger.debug("we have set the total cash in the atm to " + aCustomer.getAtm().getTotalCash());
								if(! aCustomer.isOverDraftActive()) {
									aCustomer.setOverDraftActive(true);
								}
								saveCustomer(aCustomer);
								balance = aCustomer.getBalance();
								logger.debug("balance = " + balance);
							} else {
								logger.debug("we will be over the agreed limit");
								logger.debug(FUNDS_ERR);
							} 
						} else {
							logger.debug("Atm is out of cash");
							logger.debug(ATM_ERR);
						}
					}
				}
			} else {
				logger.debug("got no customer");
				//create new customer and recurse
				String [] accountAmounts =  inputOperationArray[1].split(" ");
				int accountBalance = Integer.parseInt(accountAmounts[0].trim());
				int overDraftFacility = Integer.parseInt(accountAmounts[1].trim());
				logger.debug("about to use accountBalance : " + accountBalance);
				logger.debug("about to use overDraftFacility " + overDraftFacility);
//				Customer newCustomer = null;
//				newCustomer.setAccountNumber(accountNumber);
//				newCustomer.setPin(correctPin);
//				newCustomer.setBalance(accountBalance);
//				newCustomer.setOverdraftFacility(overDraftFacility);
				Optional<Atm> atm = findAtmById(1L);
				if(atm.isPresent()) {
					Atm aAtm = atm.get();
					logger.debug("atm id = " + aAtm.getId());
					logger.debug("atm total cash = " + aAtm.getTotalCash());
					Customer newCustomer = new Customer(accountNumber,correctPin,accountBalance,overDraftFacility);
					newCustomer.setAtm(aAtm);
					saveCustomer(newCustomer);
					createNewCustomer(inputOperationArray);
				} else{ 
					logger.debug("got no ATM");
				}
			}
		} else {
			logger.debug("pins don't match return error");
			logger.debug(ACCOUNT_ERR);
		}
		
	}
}
