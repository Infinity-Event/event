package com.capgemini.event.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capgemini.event.entities.Query;
import com.capgemini.event.entities.Response;

public interface ResponseRepo extends JpaRepository<Response, Long>{
	Optional<Response> findByQuery(Query query);
	
}
