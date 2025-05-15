package com.capgemini.event.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.capgemini.event.entities.Event;
import com.capgemini.event.entities.Feedback;
import com.capgemini.event.entities.User;
import com.capgemini.event.repositories.EventRepo;
import com.capgemini.event.repositories.FeedbackRepo;
import com.capgemini.event.repositories.UserRepo;

import jakarta.persistence.EntityNotFoundException;
@Service
public class FeedbackServiceImpl implements FeedbackService {

    private static final Logger logger = LoggerFactory.getLogger(FeedbackServiceImpl.class);

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
        logger.info("Fetching all feedbacks");
        return feedbackRepo.findAll();
    }

    @Override
    public Feedback getFeedbackById(Long id) {
        logger.info("Fetching feedback with ID: {}", id);
        return feedbackRepo.findById(id)
                .orElseThrow(() -> {
                    logger.error("Feedback not found with id: {}", id);
                    return new EntityNotFoundException("Feedback not found with id: " + id);
                });
    }

    @Override
    public Feedback createFeedback(Feedback feedback) {
        logger.info("Creating new feedback: {}", feedback);

        if (feedback.getUser() == null || feedback.getUser().getUserId() == null) {
            logger.warn("Feedback creation failed: User is null");
            throw new IllegalArgumentException("Feedback must be associated with a valid User.");
        }
        if (feedback.getEvent() == null || feedback.getEvent().getEventId() == null) {
            logger.warn("Feedback creation failed: Event is null");
            throw new IllegalArgumentException("Feedback must be associated with a valid Event.");
        }

        User user = userRepo.findById(feedback.getUser().getUserId()).orElseThrow(() -> {
            logger.error("User not found with ID: {}", feedback.getUser().getUserId());
            return new EntityNotFoundException("User not found with id: " + feedback.getUser().getUserId());
        });

        Event event = eventRepo.findById(feedback.getEvent().getEventId()).orElseThrow(() -> {
            logger.error("Event not found with ID: {}", feedback.getEvent().getEventId());
            return new EntityNotFoundException("Event not found with id: " + feedback.getEvent().getEventId());
        });

        feedback.setUser(user);
        feedback.setEvent(event);

        Feedback saved = feedbackRepo.save(feedback);
        logger.info("Feedback created successfully with ID: {}", saved.getFeedbackId());
        return saved;
    }

    @Override
    public Feedback updateFeedback(Long id, Feedback updatedFeedback) {
        logger.info("Updating feedback with ID: {}", id);
        Feedback existing = getFeedbackById(id);

        if (updatedFeedback.getUser() == null || updatedFeedback.getUser().getUserId() == null) {
            logger.warn("Feedback update failed: User is null");
            throw new IllegalArgumentException("User is required for feedback update.");
        }
        if (updatedFeedback.getEvent() == null || updatedFeedback.getEvent().getEventId() == null) {
            logger.warn("Feedback update failed: Event is null");
            throw new IllegalArgumentException("Event is required for feedback update.");
        }

        User user = userRepo.findById(updatedFeedback.getUser().getUserId()).orElseThrow(() -> {
            logger.error("User not found with ID: {}", updatedFeedback.getUser().getUserId());
            return new EntityNotFoundException("User not found with id: " + updatedFeedback.getUser().getUserId());
        });

        Event event = eventRepo.findById(updatedFeedback.getEvent().getEventId()).orElseThrow(() -> {
            logger.error("Event not found with ID: {}", updatedFeedback.getEvent().getEventId());
            return new EntityNotFoundException("Event not found with id: " + updatedFeedback.getEvent().getEventId());
        });

        existing.setRating(updatedFeedback.getRating());
        existing.setReview(updatedFeedback.getReview());
        existing.setUser(user);
        existing.setEvent(event);

        Feedback saved = feedbackRepo.save(existing);
        logger.info("Feedback updated successfully with ID: {}", saved.getFeedbackId());
        return saved;
    }

    @Override
    public Feedback patchFeedback(Long id, Feedback partialFeedback) {
        logger.info("Patching feedback with ID: {}", id);
        Feedback existing = getFeedbackById(id);

        if (partialFeedback.getRating() != 0) {
            existing.setRating(partialFeedback.getRating());
        }

        if (partialFeedback.getReview() != null) {
            existing.setReview(partialFeedback.getReview());
        }

        if (partialFeedback.getUser() != null && partialFeedback.getUser().getUserId() != null) {
            User user = userRepo.findById(partialFeedback.getUser().getUserId()).orElseThrow(() -> {
                logger.error("User not found with ID: {}", partialFeedback.getUser().getUserId());
                return new EntityNotFoundException("User not found with id: " + partialFeedback.getUser().getUserId());
            });
            existing.setUser(user);
        }

        if (partialFeedback.getEvent() != null && partialFeedback.getEvent().getEventId() != null) {
            Event event = eventRepo.findById(partialFeedback.getEvent().getEventId()).orElseThrow(() -> {
                logger.error("Event not found with ID: {}", partialFeedback.getEvent().getEventId());
                return new EntityNotFoundException("Event not found with id: " + partialFeedback.getEvent().getEventId());
            });
            existing.setEvent(event);
        }

        Feedback saved = feedbackRepo.save(existing);
        logger.info("Feedback patched successfully with ID: {}", saved.getFeedbackId());
        return saved;
    }

    @Override
    public void deleteFeedback(Long id) {
        logger.info("Deleting feedback with ID: {}", id);
        Feedback feedback = getFeedbackById(id);
        feedbackRepo.delete(feedback);
        logger.info("Feedback deleted successfully");
    }
}
