package com.capgemini.event.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capgemini.event.entities.Feedback;
public interface FeedbackRepo extends JpaRepository<Feedback, Long>{

    List<Feedback> findByEvent_EventId(Long eventId);

    List<Feedback> findByUser_UserId(Long userId);

}
