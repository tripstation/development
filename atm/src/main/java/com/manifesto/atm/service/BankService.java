package com.manifesto.atm.service;

import java.util.Optional;

import javax.transaction.Transactional;

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

	public Customer saveCustomer(Customer customer) {
		return customerRepository.save(customer);
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

}
