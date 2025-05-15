package com.capgemini.event.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


import com.capgemini.event.entities.Event;


import com.capgemini.event.entities.Query;
import com.capgemini.event.entities.User;

import com.capgemini.event.exceptions.QueryNotFoundException;
import com.capgemini.event.exceptions.UserNotFoundException;

import com.capgemini.event.repositories.EventRepo;

import com.capgemini.event.repositories.QueryRepo;
import com.capgemini.event.repositories.UserRepo;


@Service
public class QueryServiceImpl implements QueryService {
	
	private QueryRepo queryRepo;
	private UserRepo userRepo;
	private EventRepo eventRepo;
	
	@Autowired
	public QueryServiceImpl(QueryRepo queryRepo,  UserRepo userRepo, EventRepo eventRepo) {
		this.queryRepo = queryRepo;
		this.userRepo = userRepo;
		this.eventRepo = eventRepo;
	}

	@Override
	public List<Query> getAllQueries() {
		return queryRepo.findAll();
	}
	
	@Override
	public Query createEventQuery(Query query, Long userId, Long eventId) {
		User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("User Not Found!"));
		Event event= eventRepo.findById(userId).orElseThrow(() -> new RuntimeException("Event Not Found!"));
		query.setEvent(event);
		query.setUser(user);
		return queryRepo.save(query);
	}
	
	@Override
	public Query getQueryById(Long queryId) {
		return queryRepo.findById(queryId).orElseThrow(()-> new QueryNotFoundException("Query Not Found!"));
	}

	@Override
	public void deleteQuery(Long queryId) {

		Query query = queryRepo.findById(queryId).orElseThrow(()-> new QueryNotFoundException("Query Not Found!"));
		queryRepo.delete(query);
	}
}
