package com.capgemini.event.services;

import java.util.List;

import com.capgemini.event.exceptions.UserNotFoundException;
import com.capgemini.event.exceptions.QueryNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.capgemini.event.entities.Event;

import org.springframework.stereotype.Service;
import com.capgemini.event.entities.Query;
import com.capgemini.event.entities.User;
import com.capgemini.event.repositories.EventRepo;
import com.capgemini.event.repositories.QueryRepo;
import com.capgemini.event.repositories.UserRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class QueryServiceImpl implements QueryService {

	private QueryRepo queryRepo;
	private UserRepo userRepo;
	private EventRepo eventRepo;

	@Autowired
	public QueryServiceImpl(QueryRepo queryRepo, UserRepo userRepo, EventRepo eventRepo) {
		this.queryRepo = queryRepo;
		this.userRepo = userRepo;
		this.eventRepo = eventRepo;
	}

	@Override
	public List<Query> getAllQueries() {
		log.info("Fetching all queries from the repository");
		return queryRepo.findAll();
	}

	@Override
	public Query createEventQuery(Query query, Long userId, Long eventId) {
		log.info("Creating query for user ID {} and event ID {}", userId, eventId);
//		User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("User Not Found!"));
//		Event event= eventRepo.findById(userId).orElseThrow(() -> new RuntimeException("Event Not Found!"));
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
//			new QueryNotFoundException("Query Not Found!"));
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
}
