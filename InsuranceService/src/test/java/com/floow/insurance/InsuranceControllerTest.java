package com.floow.insurance;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
class InsuranceControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(InsuranceControllerTest.class);
	
	@Autowired
	private MockMvc mockMvc;
	
//	@Disabled
	@Test
	void shouldReturnAListOfAllTheDrivers() throws Exception{
		logger.debug("shouldReturnAListOfAllTheDrivers()");
		this.mockMvc.perform(MockMvcRequestBuilders
				.get("/drivers")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andDo(print()).andExpect(status().isOk())
		.andExpect(content().string(containsString("Jimi")))
		.andExpect(content().string(containsString("Hendrix")));
	}
	
//	@Disabled
	@Test
	void shouldReturnAListOfAllTheDriversCreatedAfterAGivenDate() throws Exception{
		logger.debug("shouldReturnAListOfAllTheDriversCreatedAfterAGivenDate()");
		this.mockMvc.perform(MockMvcRequestBuilders
				.get("/drivers/byDate/?date=1984-01-01"))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("Charles")))
				.andExpect(content().string(containsString("Black")));;
	}

	@Test
	void shouldCreateANewDriver() throws Exception {
		logger.debug("shouldCreateANewDriver()");
		this.mockMvc.perform(MockMvcRequestBuilders
				.post("/driver/create")
				.content("{\"firstName\": \"John\", \"lastName\": \"Smith\", \"dateOfBirth\" : \"1980-05-01\"}")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isCreated())
				.andExpect(content().string(containsString("John")))
				.andExpect(content().string(containsString("Smith")))
				.andExpect(content().string(containsString("1980-05-01")));
	}
}
