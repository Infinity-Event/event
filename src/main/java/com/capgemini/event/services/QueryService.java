package com.capgemini.event.services;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

public interface QueryService {

	List<Query> getAllQueries();
	
	Query getQueryById(Long queryId);
	
	Query createQuery(Query query);
	
	void deleteQuery(Long queryId);
	
}
