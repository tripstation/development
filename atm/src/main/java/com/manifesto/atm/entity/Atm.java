package com.manifesto.atm.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Atm {
	
	@GeneratedValue
	@Id
	private Long id;
	private int totalCash;
	@JsonIgnore
	@OneToMany(mappedBy = "atm", cascade = CascadeType.ALL)
	private List<Customer> customer; 
	
	public Atm() {
		super();
	}
	
	public Atm(int totalCash) {
		super();
		this.totalCash = totalCash;
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
}
