package com.ecsdigital.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecsdigital.entity.Car;
import com.ecsdigital.service.CarService;
import com.ecsdigital.service.DataMuseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;


@RestController
@RequestMapping("/api/v1/")
public class CarsController {
	
	private static final Logger logger = LoggerFactory.getLogger(CarsController.class);
	
	@Autowired
	private CarService carService;
	
	@Autowired
	private DataMuseService dataMuseService;
	
	@GetMapping("/car/{id}")
	public ResponseEntity<Car>getCarById(@PathVariable Long id) {
		
		Optional<Car> car = carService.findById(id);
		if(car.isPresent()) {
			logger.debug("Car model = " + car.get().getModel());
			logger.debug("call the DataMuse Service to get some names that sound like our car model");
			String words = dataMuseService.getWords(car.get().getModel());
			logger.debug("got words as " + words);
			car.get().setDescription(words);
			return new ResponseEntity<Car>(car.get(),HttpStatus.OK);
		} else {
			System.out.println("got no car");
			return new ResponseEntity<Car>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping("/car")
	ResponseEntity<Car> createCar(@RequestBody Car car) {
		logger.debug("Calling createCar");
		Car savedCar = carService.save(car);
		logger.debug("colour = " + savedCar.getColour());
		return new ResponseEntity<Car>(carService.save(car),HttpStatus.CREATED);
	}
	
	@DeleteMapping("/car/{model}")
	public ResponseEntity<Long> deleteCarByModel(@PathVariable String model) {
		return new ResponseEntity<Long>(carService.deleteByModel(model),HttpStatus.ACCEPTED);
	}
	
	@PatchMapping(path = "/car/{id}", consumes = "application/json-patch+json")
	ResponseEntity<Car> changeCar(@PathVariable Long id, @RequestBody JsonPatch carPatch) {
		logger.debug("Calling changeCar");
			Optional<Car> car = carService.findById(id);
			if(car.isPresent()) {
				logger.debug("changeCar() car is " + car.get().getModel());
				try {
					Car carPatched = carService.patchCar(carPatch, car);
					logger.debug("patched car in controller = " + carPatched.getColour());
					carService.save(carPatched);
					return new ResponseEntity<Car>(carPatched,HttpStatus.OK);
				} catch (JsonPatchException | JsonProcessingException e) {
					logger.debug(e.getMessage());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
				}
			} else {
				return new ResponseEntity<Car>(HttpStatus.NOT_FOUND);
			}
	}
}
