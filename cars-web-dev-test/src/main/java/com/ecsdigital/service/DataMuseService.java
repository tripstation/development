package com.ecsdigital.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.ecsdigital.entity.DataMuse;

import reactor.core.publisher.Mono;

@Service
public class DataMuseService {
	private static final Logger logger = LoggerFactory.getLogger(DataMuseService.class);
	private final WebClient webClient;
	private static String BASE_URL = "https://api.datamuse.com/words";
	
	public DataMuseService(WebClient.Builder webClientBuilder) {
		this.webClient = WebClient.builder().baseUrl(BASE_URL).build();
	}
	
	public String getWords(String word) {
		logger.debug("getWords got word as " + word);
		logger.debug("in getWords Base url = " + BASE_URL);
		StringBuilder sb = new StringBuilder();
		Mono<DataMuse[]> response =  this.webClient.get()
				.uri("?sl=" +  word + "&max=4")
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.bodyToMono(DataMuse[].class).log();
		
		DataMuse [] dataMuse = response.block();
		for(DataMuse data : dataMuse) {
			
			if(!word.equalsIgnoreCase(data.getWord()) ) {
				logger.debug("got " + data.getWord());
				sb.append(data.getWord()).append(" ");
			}
		}
		return sb.toString();
	}
}
