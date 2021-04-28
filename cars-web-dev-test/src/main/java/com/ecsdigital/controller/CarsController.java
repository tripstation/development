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

import com.ecsdigital.entity.Model;
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
	public ResponseEntity<Model>getMakeAndModelById(@PathVariable Long id) {
		
		Optional<Model> model = carService.findModelById(id);
		if(model.isPresent()) {
			logger.debug("Model model = " + model.get().getModel());
			logger.debug("call the DataMuse Service to get some names that sound like our car model");
			String words = dataMuseService.getWords(model.get().getModel());
			logger.debug("got words as " + words);
			model.get().setDescription(words);
			return new ResponseEntity<Model>(model.get(),HttpStatus.OK);
		} else {
			logger.debug("got no car");
			return new ResponseEntity<Model>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping("/car")
	ResponseEntity<Model> createCar(@RequestBody Model model) {
		return new ResponseEntity<Model>(carService.saveModel(model),HttpStatus.CREATED);
	}
	
	@DeleteMapping("/car/{model}")
	public ResponseEntity<Long> deleteCarByModel(@PathVariable String model) {
		return new ResponseEntity<Long>(carService.deleteByModel(model),HttpStatus.ACCEPTED);
	}
	
	@PatchMapping(path = "/car/{id}", consumes = "application/json-patch+json")
	ResponseEntity<Model> changeCar(@PathVariable Long id, @RequestBody JsonPatch modelPatch) {
			Optional<Model> model = carService.findModelById(id);
			if(model.isPresent()) {
				logger.debug("changeModel() model is " + model.get().getModel());
				try {
					Model modelPatched = carService.patchModel(modelPatch, model);
					logger.debug("patched car in controller = " + modelPatched.getColour());
					carService.saveModel(modelPatched);
					return new ResponseEntity<Model>(modelPatched,HttpStatus.OK);
				} catch (JsonPatchException | JsonProcessingException e) {
					logger.debug(e.getMessage());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
				}
			} else {
				return new ResponseEntity<Model>(HttpStatus.NOT_FOUND);
			}
	}
}
