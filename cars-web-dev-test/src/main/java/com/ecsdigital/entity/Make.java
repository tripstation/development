package com.ecsdigital.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="make")
public class Make {

	@Id
	@GeneratedValue
	private long id;
	private String make;
	@OneToMany(mappedBy = "make", cascade = CascadeType.ALL)
	private List<Model> models = new ArrayList<>();
	
	public Make() {
	}
	
	public Make(String make) {
		super();
		this.make = make;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getMake() {
		return make;
	}
	public void setMake(String make) {
		this.make = make;
	}
}
