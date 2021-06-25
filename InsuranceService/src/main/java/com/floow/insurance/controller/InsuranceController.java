package com.floow.insurance.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	private static final Logger logger = LoggerFactory.getLogger(InsuranceController.class);
	
	@Autowired
	DriverService driverService;
	
	@GetMapping("/drivers")
	public ResponseEntity<List<Driver>> GetAllDrivers() {
		logger.debug("GetAllDrivers()");
		List<Driver> drivers = driverService.getDrivers();
		return new ResponseEntity<List<Driver>>(drivers,HttpStatus.OK);
	}
	
	@GetMapping("/drivers/byDate/")
	public ResponseEntity<List<Driver>> GetAllDriversAfterGivenDate( @RequestParam("date") String date) {
		logger.debug("GetAllDrivers() " + date);
		List<Driver> drivers = driverService.getDrivers();
		drivers = driverService.GetAllDriversAfterGivenDate(date,drivers);
		return new ResponseEntity<List<Driver>>(drivers,HttpStatus.OK);
	}
	
	@PostMapping("/driver/create")
	ResponseEntity<Driver> CreateDriver(@RequestBody Driver newDriver)  {
		logger.debug("in create driver is " + newDriver);
		
		return new ResponseEntity<Driver>(driverService.saveDriver(newDriver),HttpStatus.CREATED);
	}
}
