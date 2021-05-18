package com.manifesto.atm.controller;

import java.util.Optional;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manifesto.atm.entity.Atm;
import com.manifesto.atm.entity.Customer;
import com.manifesto.atm.service.BankService;

@RestController
@RequestMapping("/api/v1/")
public class AtmController {

	@Autowired
	private BankService bankService;
 
	private static final Logger logger = LoggerFactory.getLogger(AtmController.class);
	private static final String ACCOUNT_ERR = "ACCOUNT_ERR";

	@PutMapping("/customer")
	public ResponseEntity<Customer> createCustomer (@RequestBody Customer customer) {
		return new ResponseEntity<Customer>(bankService.saveCustomer(customer),HttpStatus.CREATED);
	}
	
	@PutMapping("/atm/{id}")
	public ResponseEntity<Integer>CheckSession(@RequestBody Atm autoTeller,@PathVariable Long id) {
		Optional<Atm> atm = bankService.findAtmById(id);
		if(atm.isPresent()) {
			if(autoTeller.getTotalCash() != 0) {
				logger.debug("We have a new session got total cash in body as " + autoTeller.getTotalCash());
				// set total cash for new session
					atm.get().setTotalCash(autoTeller.getTotalCash());
			} else {
				// ongoing session
				logger.debug("We have an ongoing session got total cash from atm as " + atm.get().getTotalCash());
			}
			return new ResponseEntity<Integer>(atm.get().getTotalCash(),HttpStatus.OK);
		} else {
			// atm id not present
			return new ResponseEntity<Integer>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping(path="/textTest", consumes = MediaType.TEXT_PLAIN)
	public ResponseEntity<String> textTest(@RequestBody String bankCustomer) {
		String [] urlArray = bankCustomer.split(" "); //todo: handle new line
		logger.debug("got arg 1 as " + urlArray[0]); // atm total cash
		logger.debug("got arg 2 as " + urlArray[1]); // account number
		logger.debug("got arg 3 as " + urlArray[2]); // correct pin
		logger.debug("got arg  as " + urlArray[3]); // actual entred pin
		int account = Integer.parseInt(urlArray[1]);
		int correctPin = Integer.parseInt(urlArray[2]);
		int enteredPin = Integer.parseInt(urlArray[3]);
		Optional<Customer> customer = bankService.findCustomerByAccountNumber(account);
		if(customer.isPresent()) {
			Customer aCustomer = customer.get();
			if(enteredPin == correctPin) {
				logger.debug("pins match");
				String balance = Integer.toString(aCustomer.getBalance());
				logger.debug("returning balance as " + balance);
				return new ResponseEntity<String>(balance,HttpStatus.OK);
			} else {
				logger.debug("pins don't match return error");
				return new ResponseEntity<String>(ACCOUNT_ERR,HttpStatus.UNAUTHORIZED);
			}
		} else {
			logger.debug("got no customer");
			return new ResponseEntity<String>(ACCOUNT_ERR,HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/balance/{account}")
	public ResponseEntity<Customer>getAccountSummary(@RequestBody Customer bankCustomer, @PathVariable int account) {
		logger.debug("got arg account as " + account);
		logger.debug("got bankCustomer pin as " + bankCustomer.getPin());
		Optional<Customer> customer = bankService.findCustomerByAccountNumber(account);
		if(customer.isPresent()) {
			logger.debug("got a customer account number" + customer.get().getAccountNumber());
			if(customer.get().getPin() == bankCustomer.getPin()) {
				logger.debug("pin matches account");
			} else {
				logger.debug("pins don't match");
				return new ResponseEntity<Customer>(customer.get(),HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<Customer>(customer.get(),HttpStatus.OK);
		} else {
			//ACCOUNT_ERR
			logger.debug("could not validate customer ACCOUNT_ERR");
			return new ResponseEntity<Customer>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/customer/withdraw/{account}")
	public ResponseEntity<Customer>withdrawFunds(@RequestBody Customer bankCustomer, @PathVariable int account) {
		logger.debug("withdrawFunds()");
		logger.debug("got arg account as " + account);
		logger.debug("got bankCustomer pin as " + bankCustomer.getPin());
		Optional<Customer> customer = bankService.findCustomerByAccountNumber(account);
		if(customer.isPresent()) {
			Customer aCustomer = customer.get();
			logger.debug("total cash in atm is " + aCustomer.getAtm().getTotalCash());
			if(aCustomer.getPin() == bankCustomer.getPin()) {
				logger.debug("pin matches account");
				logger.debug("we want to withdraw " + bankCustomer.getWithdrawalAmount());
				//do withdrawal but first check the total amount of cash in the atm
				if(aCustomer.getAtm().getTotalCash() - bankCustomer.getWithdrawalAmount() > 0) {
					aCustomer.setWithdrawalAmount(bankCustomer.getWithdrawalAmount());
					aCustomer =  bankService.getCustomerBankBalance(aCustomer);
				} else {
					logger.debug("Atm is out of cash");
					return new ResponseEntity<Customer>(HttpStatus.EXPECTATION_FAILED);
				}
			} else {
				logger.debug("pins don't match");
				return new ResponseEntity<Customer>(customer.get(),HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<Customer>(customer.get(),HttpStatus.OK);
		} else {
			logger.debug("could not validate customer ACCOUNT_ERR");
			return new ResponseEntity<Customer>(HttpStatus.NOT_FOUND);
		}
	}
}
