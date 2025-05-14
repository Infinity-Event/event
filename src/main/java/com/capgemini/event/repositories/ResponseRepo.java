package com.capgemini.event.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capgemini.event.entities.Response;

public interface ResponseRepo extends JpaRepository<Response, Long>{

}
