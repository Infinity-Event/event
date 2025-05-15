package com.capgemini.event.services;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

@Service
public interface QueryService {

	List<Query> getAllQueries();
	
	Query getQueryById(Long queryId);
	
	Query createQuery(Query query);
	
	void deleteQuery(Long queryId);
	
}
