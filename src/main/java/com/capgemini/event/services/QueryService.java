package com.capgemini.event.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.capgemini.event.entities.Query;

@Service
public interface QueryService {

	List<Query> getAllQueries();
	
	Query getQueryById(Long queryId);
	
	Query createEventQuery(Query query, Long userId);
	
	void deleteQuery(Long queryId);
}
