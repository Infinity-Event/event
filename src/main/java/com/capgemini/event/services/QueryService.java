package com.capgemini.event.services;

import java.util.List;

import com.capgemini.event.entities.Query;
import org.springframework.stereotype.Service;


public interface QueryService {

	List<Query> getAllQueries();
	
	Query getQueryById(Long queryId);
	
	Query createEventQuery(Query query, Long userId);
	
	void deleteQuery(Long queryId);
	
}
