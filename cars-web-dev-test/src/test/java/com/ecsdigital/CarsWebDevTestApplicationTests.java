package com.ecsdigital;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
class CarsWebDevTestApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	private static final MediaType JSON_PATCH = new MediaType("application", "json-patch+json");
	
	@Test
	public void shouldSaveTheCorrectMakeAndModel() throws Exception {
		this.mockMvc.perform( MockMvcRequestBuilders
				.put("/api/v1/car")
				.content("{\"model\": \"XC90\",\"colour\": \"Yellow\",\"year\": 2010,\"make\": {\"id\": 1,\"make\": \"Volvo\"}}")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isCreated())
				.andExpect(content().string(containsString("XC90")))
				.andExpect(content().string(containsString("Volvo")));
	}
	
	@Test
	public void shouldReturnTheCorrectMakeAndModel() throws Exception {
		this.mockMvc.perform(get("/api/v1/car/1")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("V60")))
				.andExpect(content().string(containsString("Volvo")));
	}
	
	@Test
	public void shouldUpdateTheGivenProperty() throws Exception {
		this.mockMvc.perform( MockMvcRequestBuilders
				.patch("/api/v1/car/4")
				.content("[{\"op\": \"replace\", \"path\": \"/colour\", \"value\": \"maroon\"}]")
				.contentType(JSON_PATCH)
				.accept(JSON_PATCH))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("maroon")));
	}
	
	@Test
	public void shouldDeleteTheGivenCar() throws Exception {
		this.mockMvc.perform( MockMvcRequestBuilders
				.delete("/api/v1/car/S40"))
				.andExpect(status().isAccepted());
	}
}