package com.manifesto.atm;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.manifesto.atm.entity.Atm;
import com.manifesto.atm.entity.Customer;
import com.manifesto.atm.service.BankService;

@SpringBootTest
@AutoConfigureMockMvc

@TestInstance(Lifecycle.PER_CLASS)
class AtmControllerTest {
	
	private static final Logger logger = LoggerFactory.getLogger(AtmControllerTest.class);
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	BankService bankService;
	
	private Atm atm;
	private Customer customer;
	
	@BeforeAll
	public void setupTransaction()  {
		logger.debug("setupTransaction()");
		//set up Atm and fill it with cash
		Optional<Atm> aAtm = bankService.findAtmById(1L);
		if(aAtm.isEmpty()) {
			logger.debug("Got no atm so setting one up");
			atm = new Atm();
			atm.setTotalCash(8000);
			bankService.saveAtm(atm);
			logger.debug("got atm id as " + atm.getId() );
			logger.debug("got total cash as  " + atm.getTotalCash());
		} else {
			logger.debug("Got an atm");
			atm = aAtm.get();
			logger.debug("got atm id as " + atm.getId() );
			logger.debug("got total cash as  " + atm.getTotalCash());
		}
		
		// Create a new customer account
		customer = new Customer(88888888,1234,500,100);
		customer.setAtm(atm);
		bankService.saveCustomer(customer);
		
	}
	
	@Test
	public void ShouldReturnTheCorrectAtmTotalForANewSession() throws Exception {
		logger.debug("in ShouldReturnTheCorrectAtmTotalForANewSession()");
		this.mockMvc.perform(put("/api/v1/atm/1")
		.content("{\"totalCash\" : 8000}")
		.contentType(MediaType.APPLICATION_JSON).
		 accept(MediaType.APPLICATION_JSON))
		.andDo(print()).andExpect(status().isOk())
		.andExpect(content().string(equalTo("8000")));
	}
	
	@Test
	public void shouldSaveTheCorrectCustomerAndAtm() throws Exception {
		logger.debug("shouldSaveTheCorrectCustomerAndAtm()");
		this.mockMvc.perform( MockMvcRequestBuilders
				.put("/api/v1/customer")
				.content("{\"accountNumber\": 99999999,\"pin\": 9999,\"balance\": 500, \"overdraftFacility\" : 100,  \"atm\": {\"id\": 1,\"totalCash\": 8000}}")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isCreated())
				.andExpect(content().string(containsString("8000")))
				.andExpect(content().string(containsString("99999999")));
	}

	@Test
	public void shoudReturnCurrentBalenceAndOverDraftAndTotalCashGivenCorrectAccountNumAndPin() throws Exception{
		logger.debug("shoudReturnCurrentBalenceAndOverDraftAndTotalCashGivenCorrectAccountNumAndPin()");
		this.mockMvc.perform(MockMvcRequestBuilders
				.post("/api/v1/balance/88888888")
				.content("{\"accountNumber\": 88888888, \"pin\": 1234}")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("450")))
				.andExpect(content().string(containsString("100")))
				.andExpect(content().string(containsString("7950")));
	}
	
	@Test
	public void shouldWithdrawTheCorrectAmountGivenFundsAvailable() throws Exception {
		logger.debug("shouldWithdrawTheCorrectAmountGivenFundsAvailable()");
		this.mockMvc.perform(MockMvcRequestBuilders
				.post("/api/v1/customer/withdraw/88888888")
				.content("{\"accountNumber\": 88888888, \"pin\": 1234, \"withdrawalAmount\" : 50}")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("450")))
				.andExpect(content().string(containsString("100")))
				.andExpect(content().string(containsString("7950")));
	}

	@Test
	public void shouldWithdrawTheCorrectAmountGivenFundsAvailableAndUseTheOverDraftFacility() throws Exception {
		logger.debug("shouldWithdrawTheCorrectAmountGivenFundsAvailableAndUseTheOverDraftFacility()");
		this.mockMvc.perform(MockMvcRequestBuilders
				.post("/api/v1/customer/withdraw/88888888")
				.content("{\"accountNumber\": 88888888, \"pin\": 1234, \"withdrawalAmount\" : 501}")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("-51")))
				.andExpect(content().string(containsString("-401")))
				.andExpect(content().string(containsString("7449")));
	}
	
	@Test
	public void shoudReturnAnAccountErrorGivenInCorrectPinTextTest() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders
				.post("/api/v1/textTest/")
				.content("8000\n 88888888 1234 1235")
				.contentType(MediaType.TEXT_PLAIN)
				.accept(MediaType.TEXT_PLAIN))
				.andExpect(content().string(containsString("ACCOUNT_ERR")))
				.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	@AfterAll
	public void shouldRunTheInputFile() throws Exception {
		logger.debug("shouldRunTheInputFile()");
		List<String> atmList = bankService.parseTextFile();
		for(int i = 1; i < atmList.size(); i++) {
			String [] customerObjectArray = atmList.get(i).split("\\n");
			bankService.processAtmTransactions(customerObjectArray);
		}
		
	}
}