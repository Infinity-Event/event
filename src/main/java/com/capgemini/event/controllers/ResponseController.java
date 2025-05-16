package com.capgemini.event.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.event.entities.Response;
import com.capgemini.event.services.ResponseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/responses")
public class ResponseController {

	private ResponseService responseService;

	@Autowired
	public ResponseController(ResponseService responseService) {
		this.responseService = responseService;
	}

	@GetMapping
	public ResponseEntity<List<Response>> getAllResponses() {
		return ResponseEntity.status(HttpStatus.OK).body(responseService.getAllResponses());
	}

	@GetMapping("/{responseId}")
	public ResponseEntity<Response> getResponseById(@PathVariable Long responseId) {
		Response response = responseService.getResponseById(responseId);
		if (response != null) {
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}
	
	@GetMapping("/query/{queryId}")
    public ResponseEntity<Response> getResponseByQueryId(@PathVariable Long queryId) {
        try {
            Response response = responseService.getResponseByQueryId(queryId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

	@PostMapping
	public ResponseEntity<Response> createResponse(@Valid @RequestBody Response response, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new IllegalArgumentException("Invalid Data");
		}
		Response created = responseService.createResponse(response);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteResponse(@PathVariable Long id) {
		responseService.deleteResponse(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}