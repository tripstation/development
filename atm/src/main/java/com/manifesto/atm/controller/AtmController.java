package com.manifesto.atm.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manifesto.atm.entity.Atm;

@RestController
@RequestMapping("/api/v1/")
public class AtmController {

	@GetMapping("/customer/{id}")
	public ResponseEntity<Atm> CheckBalance() {
		return new ResponseEntity<Atm>(HttpStatus.OK);
	}
}
