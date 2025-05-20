package com.capgemini.event.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.capgemini.event.entities.Feedback;
import com.capgemini.event.exceptions.FeedbackNotFoundException;
import com.capgemini.event.repositories.FeedbackRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepo feedbackRepo;
    private static final String FEEDBACK_NOT_FOUND_MSG = "Feedback not found with id: ";

    @Override
    public List<Feedback> getAllFeedbacks() {
        log.debug("Retrieving all feedbacks");
        return feedbackRepo.findAll();
    }

    @Override
    public Feedback getFeedbackById(Long feedbackId) {
        log.debug("Retrieving feedback with id {}", feedbackId);
        return feedbackRepo.findById(feedbackId)
                .orElseThrow(() -> {
                    log.warn("Feedback with id {} not found", feedbackId);
                    return new FeedbackNotFoundException(FEEDBACK_NOT_FOUND_MSG + feedbackId);
                });
    }
    
    @Override
    public List<Feedback> getFeedbacksByEventId(Long eventId) {
        log.debug("Getting feedbacks for event ID: {}", eventId);
        List<Feedback> feedbacks = feedbackRepo.findByEvent_EventId(eventId);
        if (feedbacks.isEmpty()) {
            log.warn("No feedbacks found for event ID {}", eventId);
            throw new FeedbackNotFoundException("No feedbacks found for event ID: " + eventId);
        }
        return feedbacks;
    }

    @Override
    public List<Feedback> getFeedbacksByUserId(Long userId) {
        log.debug("Getting feedbacks for user ID: {}", userId);
        List<Feedback> feedbacks = feedbackRepo.findByUser_UserId(userId);
        if (feedbacks.isEmpty()) {
            log.warn("No feedbacks found for user ID {}", userId);
            throw new FeedbackNotFoundException("No feedbacks found for user ID: " + userId);
        }
        return feedbacks;
    }


    @Override
    public Feedback createFeedback(Feedback feedback) {
        log.debug("Creating new feedback");
        return feedbackRepo.save(feedback);
    }

    @Override
    public Feedback updateFeedback(Long feedbackId, Feedback feedback) {
        log.info("Updating feedback with id {}", feedbackId);
        Feedback existing = feedbackRepo.findById(feedbackId)
                .orElseThrow(() -> {
                    log.warn("Feedback with id {} not found for update", feedbackId);
                    return new FeedbackNotFoundException(FEEDBACK_NOT_FOUND_MSG + feedbackId);
                });
        existing.setRating(feedback.getRating());
        existing.setReview(feedback.getReview());
        existing.setUser(feedback.getUser());
        existing.setEvent(feedback.getEvent());
        return feedbackRepo.save(existing);
    }

    @Override
    public Feedback patchFeedback(Long feedbackId, Feedback feedback) {
        log.info("Patching feedback with id {}", feedbackId);
        Feedback existing = feedbackRepo.findById(feedbackId)
                .orElseThrow(() -> {
                    log.warn("Feedback with id {} not found for patch", feedbackId);
                    return new FeedbackNotFoundException(FEEDBACK_NOT_FOUND_MSG + feedbackId);
                });

        if (feedback.getRating() != null) {
            existing.setRating(feedback.getRating());
        }
        if (feedback.getReview() != null) {
            existing.setReview(feedback.getReview());
        }
        if (feedback.getUser() != null) {
            existing.setUser(feedback.getUser());
        }
        if (feedback.getEvent() != null) {
            existing.setEvent(feedback.getEvent());
        }
        return feedbackRepo.save(existing);
    }

    @Override
    public void deleteFeedback(Long feedbackId) {
        log.debug("Deleting feedback with id {}", feedbackId);
        if (!feedbackRepo.existsById(feedbackId)) {
            log.warn("Feedback with id {} not found for deletion", feedbackId);
            throw new FeedbackNotFoundException(FEEDBACK_NOT_FOUND_MSG + feedbackId);
        }
        feedbackRepo.deleteById(feedbackId);
    }
    
}
