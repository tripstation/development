package com.ecsdigital.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecsdigital.entity.Car;
import com.ecsdigital.model.CarRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;

@Transactional
@Service
public class CarService {
	
	private static final Logger logger = LoggerFactory.getLogger(CarService.class);
	
	@Autowired
	private CarRepository carRepository;

	
	public Car save(Car car) {
		return carRepository.save(car);
	}

	public Optional<Car> findById(Long id) {
		return carRepository.findById(id);
	}

	public long deleteByModel(String model) {
		return carRepository.deleteByModel(model);
	}

	public Car patchCar(JsonPatch patch, Optional<Car> targetCar) throws JsonPatchException, JsonProcessingException {
		logger.debug("called patchCar");
		final ObjectMapper mapper = new ObjectMapper();
		String jsonCar = mapper.writeValueAsString(targetCar.get());
		logger.debug("json car = " + jsonCar);
		logger.debug("patchCar() colour = " + targetCar.get().getColour());
		logger.debug("patch is " + patch.toString());
		JsonNode patched = patch.apply(mapper.convertValue(targetCar.get(), JsonNode.class));
		logger.debug("patched = " + patched);
		return mapper.treeToValue(patched, Car.class);
	}
}
