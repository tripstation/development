package com.manifesto.atm;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.manifesto.atm.service.BankService;

class CliTests {

	@Test
	void test() {
		BankService bankService = new BankService();
		List<String> atmList = bankService.parseTextFile();
		String [] customerObjectArray = atmList.get(1).split("\\n");
		bankService.createNewCustomer(customerObjectArray);
	}

}
