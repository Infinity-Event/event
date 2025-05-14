package com.capgemini.event.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.event.entities.Event;
import com.capgemini.event.entities.Feedback;
import com.capgemini.event.entities.User;
import com.capgemini.event.repositories.FeedbackRepo;
import com.capgemini.event.repositories.UserRepo;

import jakarta.persistence.EntityNotFoundException;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepo feedbackRepo;
    private final UserRepo userRepo;
    private final EventRepo eventRepo;

    @Autowired
    public FeedbackServiceImpl(FeedbackRepo feedbackRepo, UserRepo userRepo, EventRepo eventRepo) {
        this.feedbackRepo = feedbackRepo;
        this.userRepo = userRepo;
        this.eventRepo = eventRepo;
    }

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
        if (feedback.getUser() != null) {
            Long userId = feedback.getUser().getId();
            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
            feedback.setUser(user);
        }

        if (feedback.getEvent() != null) {
            Long eventId = feedback.getEvent().getId();
            Event event = eventRepo.findById(eventId)
                    .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + eventId));
            feedback.setEvent(event);
        }

        return feedbackRepo.save(feedback);
    }

    @Override
    public Feedback updateFeedback(Long id, Feedback updatedFeedback) {
        Feedback existing = getFeedbackById(id);

        existing.setRating(updatedFeedback.getRating());
        existing.setReview(updatedFeedback.getReview());

        if (updatedFeedback.getUser() != null) {
            Long userId = updatedFeedback.getUser().getId();
            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
            existing.setUser(user);
        }

        if (updatedFeedback.getEvent() != null) {
            Long eventId = updatedFeedback.getEvent().getId();
            Event event = eventRepo.findById(eventId)
                    .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + eventId));
            existing.setEvent(event);
        }

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
            Long userId = partialFeedback.getUser().getId();
            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
            existing.setUser(user);
        }

        if (partialFeedback.getEvent() != null) {
            Long eventId = partialFeedback.getEvent().getId();
            Event event = eventRepo.findById(eventId)
                    .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + eventId));
            existing.setEvent(event);
        }

        return feedbackRepo.save(existing);
    }

    @Override
    public void deleteFeedback(Long id) {
        Feedback feedback = getFeedbackById(id);
        feedbackRepo.delete(feedback);
    }
}
