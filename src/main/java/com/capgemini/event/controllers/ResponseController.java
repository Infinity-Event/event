package com.capgemini.event.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.capgemini.event.entities.Response;
import com.capgemini.event.services.ResponseService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/responses")
@Slf4j
public class ResponseController {

	private final ResponseService responseService;

	@Autowired
	public ResponseController(ResponseService responseService) {
		this.responseService = responseService;
	}

	@GetMapping
	public ResponseEntity<List<Response>> getAllResponses() {
		log.info("Fetching all responses");
		List<Response> responses = responseService.getAllResponses();
		log.debug("Total responses fetched: {}", responses.size());
		return ResponseEntity.status(HttpStatus.OK).body(responses);
	}

	@GetMapping("/{responseId}")
	public ResponseEntity<Response> getResponseById(@PathVariable Long responseId) {
		log.info("Fetching response by ID: {}", responseId);
		Response response = responseService.getResponseById(responseId);
		if (response != null) {
			log.debug("Response found: {}", response);
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
			log.warn("Response not found for ID: {}", responseId);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}
	
	@GetMapping("/query/{queryId}")
	public ResponseEntity<Response> getResponseByQueryId(@PathVariable Long queryId) {
		log.info("Fetching response by Query ID: {}", queryId);
		try {
			Response response = responseService.getResponseByQueryId(queryId);
			log.debug("Response found for query {}: {}", queryId, response);
			return ResponseEntity.ok(response);
		} catch (RuntimeException e) {
			log.warn("No response found for Query ID: {}", queryId);
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping("/query/{queryId}")
	public ResponseEntity<Response> createResponse(@Valid @RequestBody Response response, @PathVariable Long queryId, BindingResult bindingResult) {
		log.info("Creating new response: {}", response);
		if (bindingResult.hasErrors()) {
			log.error("Validation errors while creating response: {}", bindingResult.getAllErrors());
			throw new IllegalArgumentException(bindingResult.getFieldErrors().toString());
		}
		Response created = responseService.createResponse(response, queryId);
		log.debug("Response created: {}", created);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteResponse(@PathVariable Long id) {
		log.info("Deleting response with ID: {}", id);
		responseService.deleteResponse(id);
		log.debug("Response deleted with ID: {}", id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
