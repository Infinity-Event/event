package com.capgemini.event.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capgemini.event.entities.FeedBack;

public interface FeedBackRepo extends JpaRepository<FeedBack, Long>{

}
