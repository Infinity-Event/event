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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/queries")
public class QueryController {

	private QueryService queryService;

	@Autowired
	public QueryController(QueryService queryService) {
		this.queryService = queryService;
	}

	@GetMapping
	public ResponseEntity<List<Query>> getAllQueries() {
		log.info("Received request to fetch all queries");
		return ResponseEntity.status(HttpStatus.OK).body(queryService.getAllQueries());
	}

	@GetMapping("/{queryId}")
	public ResponseEntity<Query> getQueryById(@PathVariable Long queryId) {
		log.info("Received request to fetch query with ID: {}", queryId);
		Query query = queryService.getQueryById(queryId);
		log.debug("Query fetched: {}", query);
		return ResponseEntity.status(HttpStatus.OK).body(query);
	}

	@GetMapping("/pending/organizer/{organizerId}")
	public ResponseEntity<List<Query>> getQueriesByOrganizerId(@PathVariable Long organizerId) {
		log.info("Received request to fetch queries for organizer with ID: {}", organizerId);
		List<Query> queries = queryService.getQueriesByOrganizerId(organizerId);
		log.debug("Fetched {} queries for organizer ID: {}", queries.size(), organizerId);
		return ResponseEntity.status(HttpStatus.OK).body(queries);
	}

	@PostMapping("event/{eventId}/user/{userId}")
	public ResponseEntity<Query> createQuery(@Valid @RequestBody Query query, BindingResult bindingResult,
			@PathVariable Long eventId, @PathVariable Long userId) {
		log.info("Received request to create query: {}", query);
		if (bindingResult.hasErrors()) {
			throw new IllegalArgumentException(bindingResult.getFieldErrors().toString());
		}
		Query savedQuery = queryService.createEventQuery(query, userId, eventId);
		log.debug("Query created with ID: {}", savedQuery.getQueryId());
		return ResponseEntity.status(HttpStatus.CREATED).body(savedQuery);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteQuery(@PathVariable Long id) {
		log.info("Received request to delete query with ID: {}", id);
		queryService.deleteQuery(id);
		log.info("Query with ID {} successfully deleted", id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
