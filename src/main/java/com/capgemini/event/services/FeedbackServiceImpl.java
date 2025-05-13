package com.capgemini.event.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.event.entities.Feedback;
import com.capgemini.event.repositories.FeedbackRepo;

import jakarta.persistence.EntityNotFoundException;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    private FeedbackRepo feedbackRepo;

    @Override
    public List<Feedback> getAllFeedbacks() {
        return feedbackRepo.findAll();
    }

    @Override
    public Feedback getFeedbackById(Long id) {
        return feedbackRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Feedback not found with id: " + id));
    }

    @Override
    public Feedback createFeedback(Feedback feedback) {
        return feedbackRepo.save(feedback);
    }

    @Override
    public Feedback updateFeedback(Long id, Feedback updatedFeedback) {
        Feedback existing = getFeedbackById(id);
        existing.setRating(updatedFeedback.getRating());
        existing.setReview(updatedFeedback.getReview());
        existing.setUser(updatedFeedback.getUser());
        existing.setEvent(updatedFeedback.getEvent());
        return feedbackRepo.save(existing);
    }

    @Override
    public Feedback patchFeedback(Long id, Feedback partialFeedback) {
        Feedback existing = getFeedbackById(id);
        if (partialFeedback.getRating() != 0) {
            existing.setRating(partialFeedback.getRating());
        }
        if (partialFeedback.getReview() != null) {
            existing.setReview(partialFeedback.getReview());
        }
        if (partialFeedback.getUser() != null) {
            existing.setUser(partialFeedback.getUser());
        }
        if (partialFeedback.getEvent() != null) {
            existing.setEvent(partialFeedback.getEvent());
        }
        return feedbackRepo.save(existing);
    }

    @Override
    public void deleteFeedback(Long id) {
        Feedback feedback = getFeedbackById(id);
        feedbackRepo.delete(feedback);
    }
}
