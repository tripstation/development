package com.manifesto.atm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
public class Customer {
	
	@GeneratedValue
	@Id
	private Long id;
	private int accountNumber;
	private int pin;
	private int balance;
	private int overdraftFacility;
	@Column(nullable = true)
	private boolean overDraftActive;
	
	@Transient
	private int withdrawalAmount;
	
	@ManyToOne
	@JoinColumn(name = "atm_id")
	private Atm atm;

	
	public Customer() {
		super();
	}
	
	

	public Customer(int accountNumber, int pin, int balance, int overdraftFacility) {
		super();
		this.accountNumber = accountNumber;
		this.pin = pin;
		this.balance = balance;
		this.overdraftFacility = overdraftFacility;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
	}

	public int getPin() {
		return pin;
	}

	public void setPin(int pin) {
		this.pin = pin;
	}

	public int getWithdrawalAmount() {
		return withdrawalAmount;
	}

	public void setWithdrawalAmount(int withdrawalAmount) {
		this.withdrawalAmount = withdrawalAmount;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public int getOverdraftFacility() {
		return overdraftFacility;
	}

	public void setOverdraftFacility(int overdraftFacility) {
		this.overdraftFacility = overdraftFacility;
	}


	public boolean isOverDraftActive() {
		return overDraftActive;
	}

	public void setOverDraftActive(boolean overDraftActive) {
		this.overDraftActive = overDraftActive;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", accountNumber=" + accountNumber + ", pin=" + pin + ", balance=" + balance
				+ ", overdraftFacility=" + overdraftFacility + ", overDraftActive=" + overDraftActive
				+ ", withdrawalAmount=" + withdrawalAmount + ", atm=" + atm + "]";
	}

	public Atm getAtm() {
		return atm;
	}

	public void setAtm(Atm atm) {
		this.atm = atm;
	}
	
	
}
