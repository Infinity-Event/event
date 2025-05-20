package com.capgemini.event.services;

import java.util.List;
import java.util.stream.Collectors;

import com.capgemini.event.exceptions.UserNotFoundException;
import com.capgemini.event.exceptions.QueryNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import com.capgemini.event.entities.Event;

import org.springframework.stereotype.Service;
import com.capgemini.event.entities.Query;
import com.capgemini.event.entities.Response;
import com.capgemini.event.entities.User;
import com.capgemini.event.repositories.EventRepo;
import com.capgemini.event.repositories.QueryRepo;
import com.capgemini.event.repositories.ResponseRepo;
import com.capgemini.event.repositories.UserRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class QueryServiceImpl implements QueryService {

	private QueryRepo queryRepo;
	private UserRepo userRepo;
	private EventRepo eventRepo;
	private ResponseRepo responseRepo;

	@Autowired
	public QueryServiceImpl(QueryRepo queryRepo, UserRepo userRepo, EventRepo eventRepo, ResponseRepo responseRepo) {
		this.queryRepo = queryRepo;
		this.userRepo = userRepo;
		this.eventRepo = eventRepo;
		this.responseRepo = responseRepo;
	}

	@Override
	public List<Query> getAllQueries() {
		log.info("Fetching all queries from the repository");
		return queryRepo.findAll();
	}

	@Override
	public Query createEventQuery(Query query, Long userId, Long eventId) {
		log.info("Creating query for user ID {} and event ID {}", userId, eventId);
		User user = userRepo.findById(userId).orElseThrow(() -> {
			log.warn("User not found with ID: {}", userId);
			return new UserNotFoundException("User Not Found!");
		});

		Event event = eventRepo.findById(eventId).orElseThrow(() -> {
			log.warn("Event not found with ID: {}", eventId);
			return new RuntimeException("Event Not Found!");
		});
		query.setEvent(event);
		query.setUser(user);
		log.debug("Saving query: {}", query);
		return queryRepo.save(query);
	}

	@Override
	public Query getQueryById(Long queryId) {
		log.info("Retrieving query by ID: {}", queryId);
		return queryRepo.findById(queryId).orElseThrow(() -> {
			log.warn("Query not found with ID: {}", queryId);
			return new QueryNotFoundException("Query Not Found!");
		});
	}

	@Override
	public void deleteQuery(Long queryId) {
		log.info("Attempting to delete query with ID: {}", queryId);
		Query query = queryRepo.findById(queryId).orElseThrow(() -> {
			log.warn("Query not found with ID: {}", queryId);
			return new QueryNotFoundException("Query Not Found!");
		});
		queryRepo.delete(query);
		log.info("Query deleted with ID: {}", queryId);
	}

	@Override
	public List<Query> getQueriesByOrganizerId(Long organizerId) {
		log.info("Fetching queries for organizer with ID: {}", organizerId);

		// Find the organizer user
		User organizer = userRepo.findById(organizerId).orElseThrow(() -> {
			log.warn("Organizer not found with ID: {}", organizerId);
			return new UserNotFoundException("Organizer Not Found!");
		});

		// Get all events associated with this organizer
		List<Event> organizerEvents = eventRepo.findByOrganizer(organizer);
		log.debug("Found {} events for organizer ID: {}", organizerEvents.size(), organizerId);

		// Get all queries for these events
		List<Query> queries = queryRepo.findAll().stream()
				.filter(query -> query.getEvent() != null &&
						organizerEvents.stream()
								.anyMatch(event -> event.getEventId().equals(query.getEvent().getEventId())))
				.collect(Collectors.toList());

		log.debug("Found {} queries for organizer ID: {}", queries.size(), organizerId);
		return queries;
	}

	@Override
	public Query updateQuery(Long queryId, Query queryDetails) {
		log.info("Updating query with ID: {}", queryId);

		Query query = queryRepo.findById(queryId).orElseThrow(() -> {
			log.warn("Query not found with ID: {}", queryId);
			return new QueryNotFoundException("Query Not Found!");
		});

		// Update fields
		if (queryDetails.getQueryBody() != null) {
			query.setQueryBody(queryDetails.getQueryBody());
		}

		if (queryDetails.getStatus() != null) {
			query.setStatus(queryDetails.getStatus());
		}

		if (queryDetails.getQueryDate() != null) {
			query.setQueryDate(queryDetails.getQueryDate());
		}

		// Update response if provided
		if (queryDetails.getResponse() != null) {
			Response response = queryDetails.getResponse();

			// If response has an ID, it exists in DB
			if (response.getResponseId() != null) {
				// Update existing response
				Response existingResponse = responseRepo.findById(response.getResponseId())
						.orElseThrow(() -> new RuntimeException("Response not found"));

				existingResponse.setResponseBody(response.getResponseBody());
				existingResponse.setResponseDate(response.getResponseDate());
				responseRepo.save(existingResponse);
				query.setResponse(existingResponse);
			} else {
				// New response
				response.setQuery(query);
				Response savedResponse = responseRepo.save(response);

				query.setResponse(savedResponse);
			}
		}

		log.debug("Saving updated query: {}", query);
		return queryRepo.save(query);
	}
}
