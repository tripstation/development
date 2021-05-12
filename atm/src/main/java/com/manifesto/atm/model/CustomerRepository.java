package com.manifesto.atm.model;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.manifesto.atm.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>{

	Optional<Customer> findByAccountNumber(int account);

}
