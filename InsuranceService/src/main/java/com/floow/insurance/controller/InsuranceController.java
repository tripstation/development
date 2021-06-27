package com.floow.insurance.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.floow.insurance.model.Driver;
import com.floow.insurance.service.DriverService;


@RestController
public class InsuranceController {
	
	@Autowired
	DriverService driverService;
	
	@GetMapping("/drivers")
	public ResponseEntity<List<Driver>> GetAllDrivers() {
		return new ResponseEntity<List<Driver>>(driverService.getDrivers(),HttpStatus.OK);
	}
	
	@GetMapping("/drivers/byDate")
	public ResponseEntity<List<Driver>> GetAllDriversAfterGivenDate( @RequestParam("date") String date) {
		return new ResponseEntity<List<Driver>>(driverService.GetAllDriversAfterGivenDate(date),HttpStatus.OK);
	}
	
	@PostMapping("/driver/create")
	ResponseEntity<Driver> CreateDriver(@RequestBody Driver newDriver)  {
		return new ResponseEntity<Driver>(driverService.saveDriver(newDriver),HttpStatus.CREATED);
	}
}
