package com.capgemini.event.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capgemini.event.entities.Feedback;
public interface FeedbackRepo extends JpaRepository<Feedback, Long>{

    // Find all feedbacks for a specific event (by eventId)
    List<Feedback> findByEvent_EventId(Long eventId);

    // Find all feedbacks by a specific user (by userId)
    List<Feedback> findByUser_UserId(Long userId);

}
