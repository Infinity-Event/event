package com.capgemini.event.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.event.entities.Query;
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
	
	@GetMapping("/{queryId}")
    public ResponseEntity<Query> getQueryById(@PathVariable Long queryId) {
		 Query query = queryService.getQueryById(queryId);
		 return ResponseEntity.ok(query);
	}
	
	@PostMapping("/user/{userId}")
    public ResponseEntity<Query> creatEventeQuery(@Valid @RequestBody Query query, @PathVariable Long userId) {
        Query savedQuery = queryService.createEventQuery(query, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedQuery);
    }
	
	@DeleteMapping("/{queryId}")
    public ResponseEntity<Void> deleteQuery(@PathVariable Long queryId) {
        queryService.deleteQuery(queryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
