package com.capgemini.event.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capgemini.event.entities.Feedback;

public interface FeedbackRepo extends JpaRepository<Feedback, Long>{

}
