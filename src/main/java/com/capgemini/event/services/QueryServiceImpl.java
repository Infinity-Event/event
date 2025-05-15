package com.capgemini.event.services;

import java.util.List;

import com.capgemini.event.exceptions.UserNotFoundException;
import com.capgemini.event.exceptions.QueryNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.event.entities.Query;
import com.capgemini.event.entities.User;

import com.capgemini.event.repositories.QueryRepo;
import com.capgemini.event.repositories.UserRepo;

@Service
public class QueryServiceImpl implements QueryService {
	
	private QueryRepo queryRepo;
	private UserRepo userRepo;
	
	@Autowired
	public QueryServiceImpl(QueryRepo queryRepo,  UserRepo userRepo) {
		this.queryRepo = queryRepo;
		this.userRepo = userRepo;
	}

	@Override
	public List<Query> getAllQueries() {
		return queryRepo.findAll();
	}
	
	@Override
	public Query createEventQuery(Query query, Long userId) {
		User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("User Not Found!"));
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
