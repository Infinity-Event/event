package com.capgemini.event.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import com.capgemini.event.repositories.QueryRepo;
@Service
public class QueryServiceImpl implements QueryService {
	
	private QueryRepo queryRepo;
	
	@Autowired
	public QueryServiceImpl(QueryRepo queryRepo) {
		this.queryRepo = queryRepo;
	}

	@Override
	public List<Query> getAllQueries() {
		return queryRepo.findAll();
	}
	
	@Override
	public Query createQuery(Query query) {
		return queryRepo.save(query);
	}
	
	@Override
	public Query getQueryById(Long queryId) {
		return queryRepo.findById(queryId).orElseThrow(()-> new RuntimeException("Query Not Found!"));
	}

	@Override
	public void deleteQuery(Long queryId) {

		Query query = queryRepo.findById(queryId).orElseThrow(()-> new RuntimeException("Query Not Found!"));
		
		queryRepo.delete(query);
	}
}
