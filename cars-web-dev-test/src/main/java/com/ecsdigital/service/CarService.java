package com.ecsdigital.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecsdigital.entity.Model;
import com.ecsdigital.model.ModelRepository;
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
	ModelRepository modelRepository;
	
	public Model saveModel(Model model) {
		return modelRepository.save(model);
	}
	public Optional<Model> findModelById( Long id) {
		return modelRepository.findById(id);
	}
	
	public long deleteByModel(String model) {
		return modelRepository.deleteByModel(model);
	}

	public Model patchModel(JsonPatch patch, Optional<Model> targetModel) throws JsonPatchException, JsonProcessingException {
		logger.debug("called patchModel");
		final ObjectMapper mapper = new ObjectMapper();
		JsonNode patched = patch.apply(mapper.convertValue(targetModel.get(), JsonNode.class));
		logger.debug("patched = " + patched);
		return mapper.treeToValue(patched, Model.class);
	}
}