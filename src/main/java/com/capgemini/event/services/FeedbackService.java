package com.capgemini.event.services;

import com.capgemini.event.entities.Feedback;

import java.util.List;

public interface FeedbackService {
    
    List<Feedback> getAllFeedbacks();

    Feedback getFeedbackById(Long feedbackId);

    Feedback createFeedback(Feedback feedback);

    Feedback updateFeedback(Long feedbackId, Feedback feedback);

    Feedback patchFeedback(Long feedbackId, Feedback feedback);

    void deleteFeedback(Long feedbackId);
    
    List<Feedback> getFeedbacksByEventId(Long eventId);
    List<Feedback> getFeedbacksByUserId(Long userId);
    
}
