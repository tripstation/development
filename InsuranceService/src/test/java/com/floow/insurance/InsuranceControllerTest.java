package com.floow.insurance;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
class InsuranceControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	void shouldReturnAListOfAllTheDrivers() throws Exception{
		this.mockMvc.perform(MockMvcRequestBuilders
				.get("/drivers")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andDo(print()).andExpect(status().isOk())
		.andExpect(content().string(containsString("Smith")))
		.andExpect(content().string(containsString("Brown")))
		.andExpect(content().string(containsString("Hendrix")));
	}
	
	@Test
	void shouldReturnAListOfAllTheDriversCreatedAfterAGivenDate() throws Exception{
		this.mockMvc.perform(MockMvcRequestBuilders
				.get("/drivers/byDate?date=1984-01-01"))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("Sally")))
				.andExpect(content().string(containsString("Brown")));;
	}

	@Test
	void shouldCreateANewDriver() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders
				.post("/driver/create")
				.content("{\"firstName\": \"John\", \"lastName\": \"Black\", \"dateOfBirth\" : \"1980-05-01\"}")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isCreated())
				.andExpect(content().string(containsString("John")))
				.andExpect(content().string(containsString("Black")))
				.andExpect(content().string(containsString("1980-05-01")));
	}
}