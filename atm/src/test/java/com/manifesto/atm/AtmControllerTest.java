package com.manifesto.atm;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.manifesto.atm.service.BankService;

@SpringBootTest
@AutoConfigureMockMvc
class AtmControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	
	
	@Test
	public static void ShouldCorrectlyParseTheInputFile() throws Exception {
		BankService bankService = new BankService();
		bankService.parseTextFile();
	}
	
	
	@Disabled
	@Test
	public void ShouldReturnTheCorrectAtmTotalForANewSession() throws Exception {
		this.mockMvc.perform(put("/api/v1/atm/1")
		.content("{\"totalCash\" : 8000}")
		.contentType(MediaType.APPLICATION_JSON).
		 accept(MediaType.APPLICATION_JSON))
		.andDo(print()).andExpect(status().isOk())
		.andExpect(content().string(equalTo("8000")));
	}
	
	@Disabled
	@Test
	public void ShouldReturnTheCorrectAtmTotalForAnOngoingSession() throws Exception {
		this.mockMvc.perform(put("/api/v1/atm/1")
		.content("{\"totalCash\" : \"\"}")
		.contentType(MediaType.APPLICATION_JSON).
		 accept(MediaType.APPLICATION_JSON))
		.andDo(print()).andExpect(status().isOk())
		.andExpect(content().string(equalTo("8000")));
	}
	
	@Disabled
	@Test
	public void ShouldReturnTheTotalCashHeldInTheAtmMacchine() throws Exception {
		this.mockMvc.perform(get("/api/v1/customer/1")).andDo(print()).andExpect(status().isOk())
		.andExpect(content().string(containsString("8000")));
	}
	
	
	@Disabled
	@Test
	public void shouldSaveTheCorrectCustomerAndAtm() throws Exception {
		this.mockMvc.perform( MockMvcRequestBuilders
				.put("/api/v1/customer")
				.content("{\"accountNumber\": 99999999,\"pin\": 9999,\"balance\": 500, \"overdraftFacility\" : 100,  \"atm\": {\"id\": 1,\"totalCash\": 8000}}")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isCreated())
				.andExpect(content().string(containsString("8000")))
				.andExpect(content().string(containsString("99999999")));
	}
	
//	@Disabled
//	@Test
//	public void ShouldReturnTheBankBalanceForTheGivenCustomer() throws Exception {
////		12345678 1234	 1234
//		this.mockMvc.perform(MockMvcRequestBuilders
//				.post("/api/v1/balance/12345678")
//				.content("{\"accountNumber\": 12345678, \"pin\": 1234}")
//				.contentType(MediaType.APPLICATION_JSON)
//				.accept(MediaType.APPLICATION_JSON))
//				.andDo(print()).andExpect(status().isOk())
//				.andExpect(content().string(containsString("B")));
//	}	
	
	
	@Disabled
	@Test
	public void shouldWithdrawTheCorrectAmountGivenFundsAvailable() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders
				.post("/api/v1/customer/withdraw/99999999")
				.content("{\"accountNumber\": 99999999, \"pin\": 9999, \"withdrawalAmount\" : 1}")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("597")));
	}
	
	
	@Disabled
	@Test
	public void shoudReturnCurrentBalenceAndOverDraftAndTotalCashGivenCorrectAccountNumAndPin() throws Exception{
//		12345678 1234	 1234
		this.mockMvc.perform(MockMvcRequestBuilders
				.post("/api/v1/balance/12345678")
				.content("{\"accountNumber\": 12345678, \"pin\": 1234}")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("496")))
				.andExpect(content().string(containsString("7993")))
				.andExpect(content().string(containsString("100")));
	}
	
//	8000
//	12345678 1234 1234
	@Disabled
	@Test
	public void ShouldIndicateABalanceHasBeenRequestedtextTest() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders
				.post("/api/v1/textTest/")
				.content("8000\n 12345678 1234 1234")
				.contentType(MediaType.TEXT_PLAIN)
				.accept(MediaType.TEXT_PLAIN))
				.andExpect(content().string(containsString("496")))
				.andDo(print()).andExpect(status().isOk());
	}
	@Disabled
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
	
	@Disabled
	@Test
	public void shoudReturnCurrentBalenceAndOverDraftAndTotalCashGivenCorrectAccountNumAndPinWenText() throws Exception{
//		12345678 1234	 1234
		this.mockMvc.perform(MockMvcRequestBuilders
				.post("/api/v1/balanceText/12345678")
//				.content("{\"accountNumber\": 12345678, \"pin\": 1234}")
//				.content("accountNumber : 12345678, pin: 1234")
				.contentType(MediaType.TEXT_PLAIN)
				.accept(MediaType.TEXT_PLAIN))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("496")))
				.andExpect(content().string(containsString("7993")))
				.andExpect(content().string(containsString("100")));
	}
	
//	.content("8000\n 12345678 1234 1234")
//	.content("{\"accountNumber\": 99999999, \"pin\": 9999, \"withdrawalAmount\" : 1}")
	@Disabled
	@Test
	public void shouldWithdrawTheCorrectAmountGivenFundsAvailableText() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders
				.post("/api/v1/customer/withdrawText")
				.content("8000\\n 99999999 1234 1234 1")
				.contentType(MediaType.TEXT_PLAIN)
				.accept(MediaType.TEXT_PLAIN))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("597")));
	}
	
}
