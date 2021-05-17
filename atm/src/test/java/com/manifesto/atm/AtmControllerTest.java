package com.manifesto.atm;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.manifesto.atm.entity.Atm;
import com.manifesto.atm.service.BankService;

@SpringBootTest
@AutoConfigureMockMvc
class AtmControllerTest {
	
	private static final Logger logger = LoggerFactory.getLogger(AtmControllerTest.class);
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	BankService bankService;
	
	private Atm atm;
	
	@BeforeEach
	public void setupTransaction()  {
		Optional<Atm> aAtm = bankService.findAtmById(1L);
		if(aAtm.isEmpty()) {
			atm = new Atm();
			atm.setTotalCash(8000);
			bankService.saveAtm(atm);
		} else {
			atm = aAtm.get();
			logger.debug("got atm id as " + atm.getId() );
			logger.debug("got total cash as  " + atm.getTotalCash());
		}
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
		this.mockMvc.perform( MockMvcRequestBuilders
				.put("/api/v1/customer")
				.content("{\"accountNumber\": 88888888,\"pin\": 9999,\"balance\": 500, \"overdraftFacility\" : 100,  \"atm\": {\"id\": 1,\"totalCash\": 8000}}")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isCreated())
				.andExpect(content().string(containsString("8000")))
				.andExpect(content().string(containsString("88888888")));
	}
	
	@Test
	public void shouldWithdrawTheCorrectAmountGivenFundsAvailable() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders
				.post("/api/v1/customer/withdraw/99999999")
				.content("{\"accountNumber\": 99999999, \"pin\": 9999, \"withdrawalAmount\" : 1}")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("94")));
	}
	
	@Test
	public void shoudReturnCurrentBalenceAndOverDraftAndTotalCashGivenCorrectAccountNumAndPin() throws Exception{
		this.mockMvc.perform(MockMvcRequestBuilders
				.post("/api/v1/balance/12345678")
				.content("{\"accountNumber\": 12345678, \"pin\": 1234}")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("400")))
				.andExpect(content().string(containsString("7790")))
				.andExpect(content().string(containsString("100")));
	}
	
	@Test
	public void shoudReturnAnAccountErrorGivenInCorrectPinTextTest() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders
				.post("/api/v1/textTest/")
				.content("8000\n 12345678 1234 1235")
				.contentType(MediaType.TEXT_PLAIN)
				.accept(MediaType.TEXT_PLAIN))
				.andExpect(content().string(containsString("ACCOUNT_ERR")))
				.andDo(print()).andExpect(status().isUnauthorized());
	}
}