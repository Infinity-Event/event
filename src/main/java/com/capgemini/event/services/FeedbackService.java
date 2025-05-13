package com.capgemini.event.services;

import com.capgemini.event.entities.Feedback;

import java.util.List;

public interface FeedbackService {
    
    List<Feedback> getAllFeedbacks();

    Feedback getFeedbackById(Long id);

    Feedback createFeedback(Feedback feedback);

    Feedback updateFeedback(Long id, Feedback feedback);

    Feedback patchFeedback(Long id, Feedback feedback);

    void deleteFeedback(Long id);
}
