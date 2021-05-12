package com.manifesto.atm.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
	
//	@GetMapping("/customer/balance/{id}")
//	public ResponseEntity<Integer> CheckBalance(@PathVariable Long id) {
//		logger.debug("in CheckBalance");
//		Optional<Atm> atm = bankService.findCustomerById(id);
//		if(atm.isPresent()) {
//			logger.debug("got balance as " + atm.get().getBalance());
//			return new ResponseEntity<Integer>(atm.get().getBalance(),HttpStatus.OK);
//		} else {
//			return new ResponseEntity<Integer>(HttpStatus.NOT_FOUND);
//		}
//	}
	
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
					int balance =  (aCustomer.isOverDraftActive()) ?   aCustomer.getBalance() :  aCustomer.getBalance() + aCustomer.getOverdraftFacility();
					logger.debug("we just balance to " + balance);
					logger.debug("is overdraft active " + aCustomer.isOverDraftActive());
					if(bankCustomer.getWithdrawalAmount() < balance) {
	//					//check withdrawl amount is less than balance + overdraft
						logger.debug("we will still be in credit");
						aCustomer.setBalance(balance - bankCustomer.getWithdrawalAmount());
						logger.debug("account balance = " + aCustomer.getBalance());
						//create a new customer object with the new balance and save it
						//decrement atm cash total by withdrawal amount
						aCustomer.getAtm().setTotalCash(aCustomer.getAtm().getTotalCash() - bankCustomer.getWithdrawalAmount());
						logger.debug("we have set the total cash in the atm to " + aCustomer.getAtm().getTotalCash());
						if(! aCustomer.isOverDraftActive()) {
							aCustomer.setOverDraftActive(true);
						}
						bankService.saveCustomer(aCustomer);
						
						return new ResponseEntity<Customer>(aCustomer,HttpStatus.OK);
				} else {
					logger.debug("we will be over the agreed limit");
					}
				} else {
					logger.debug("Atm is out of cash");
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
