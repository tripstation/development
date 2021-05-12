package com.manifesto.atm.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Atm {
	
	@GeneratedValue
	@Id
	private Long id;
//	private int accountNumber;
	private int totalCash;
//	private int pin;
//	private int balance;
//	private int overdraftFacility;
	
//	@Transient
//	private int withdrawalAmount; 
	
	@JsonIgnore
	@OneToMany(mappedBy = "atm", cascade = CascadeType.ALL)
	private List<Customer> customer; 
	
	
	public Atm() {
		super();
	}

	public List<Customer> getCustomer() {
		return customer;
	}


	public void setCustomer(List<Customer> customer) {
		this.customer = customer;
	}


	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getTotalCash() {
		return totalCash;
	}
	public void setTotalCash(int totalCash) {
		this.totalCash = totalCash;
	}
//	public int getAccountNumber() {
//		return accountNumber;
//	}
//	public void setAccountNumber(int accountNumber) {
//		this.accountNumber = accountNumber;
//	}
//	public int getPin() {
//		return pin;
//	}
//	public void setPin(int pin) {
//		this.pin = pin;
//	}
//	public int getBalance() {
//		return balance;
//	}
//	public void setBalance(int balance) {
//		this.balance = balance;
//	}
//
//	public int getOverdraftFacility() {
//		return overdraftFacility;
//	}
//
//	public void setOverdraftFacility(int overdraftFacility) {
//		this.overdraftFacility = overdraftFacility;
//	}

//	public int getWithdrawalAmount() {
//		return withdrawalAmount;
//	}
//
//	public void setWithdrawalAmount(int withdrawalAmount) {
//		this.withdrawalAmount = withdrawalAmount;
//	}
	
}
