package com.capgemini.event.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import com.capgemini.event.entities.Query;
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

import com.capgemini.event.services.QueryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/queries")
public class QueryController {
	
	private QueryService queryService;
	
	@Autowired
	public QueryController(QueryService queryService) {
		this.queryService = queryService;
	}

	@GetMapping
	public ResponseEntity<List<Query>> getAllQueries(){
		return ResponseEntity.status(HttpStatus.OK).body(queryService.getAllQueries());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Query> getQueryById(@PathVariable Long id) {
    		Query query = queryService.getQueryById(id);
    		return ResponseEntity.status.(HttpStatus.OK).body(query);
}
	
	@PostMapping("/user/{userId}")
    public ResponseEntity<Query> createQuery(@Valid @RequestBody Query query, BindingResult bindingResult, @PathVariable Long userId) {
		if(bindingResult.hasErrors() ) {
			throw new IllegalArgumentException("Invalid Data");
		}
        Query savedQuery = queryService.createEventQuery(query, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedQuery);
    }
	
	@DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuery(@PathVariable Long id) {
        queryService.deleteQuery(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
