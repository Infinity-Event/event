package com.capgemini.event.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.capgemini.event.entities.Feedback;
import com.capgemini.event.services.FeedbackService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/feedbacks")
@Slf4j
public class FeedbackRestController {

    private final FeedbackService feedbackService;
    
    @Autowired
    public FeedbackRestController(FeedbackService feedbackService) {
		this.feedbackService = feedbackService;
	}

	@GetMapping
    public ResponseEntity<List<Feedback>> getAllFeedbacks() {
        log.info("Fetching all feedbacks");
        List<Feedback> feedbacks = feedbackService.getAllFeedbacks();
        log.debug("Total feedbacks found: {}", feedbacks.size());
        return ResponseEntity.ok(feedbacks);
    }

    @GetMapping("/{feedbackId}")
    public ResponseEntity<Feedback> getFeedbackById(@PathVariable Long feedbackId) {
        log.info("Fetching feedback with ID: {}", feedbackId);
        Feedback feedback = feedbackService.getFeedbackById(feedbackId);
        log.debug("Feedback fetched: {}", feedback);
        return ResponseEntity.ok(feedback);
    }
    
    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<Feedback>> getFeedbacksByEventId(@PathVariable Long eventId) {
        log.info("Fetching feedbacks for event ID: {}", eventId);
        List<Feedback> feedbacks = feedbackService.getFeedbacksByEventId(eventId);
        log.debug("Total feedbacks for event {}: {}", eventId, feedbacks.size());
        return ResponseEntity.ok(feedbacks);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Feedback>> getFeedbacksByUserId(@PathVariable Long userId) {
        log.info("Fetching feedbacks for user ID: {}", userId);
        List<Feedback> feedbacks = feedbackService.getFeedbacksByUserId(userId); 
        log.debug("Total feedbacks for user {}: {}", userId, feedbacks.size());
        return ResponseEntity.ok(feedbacks);
    }


    @PostMapping
    public ResponseEntity<Feedback> createFeedback(@Valid @RequestBody Feedback feedback, BindingResult bindingResult) {
        log.info("Creating feedback: {}", feedback);
        if (bindingResult.hasErrors()) {
            log.warn("Invalid feedback data: {}", bindingResult.getAllErrors());
            throw new IllegalArgumentException("Invalid Data");
        }
        Feedback created = feedbackService.createFeedback(feedback);
        log.debug("Created feedback with ID: {}", created.getFeedbackId());
        return ResponseEntity.created(URI.create("/api/feedbacks/" + created.getFeedbackId())).body(created);
    }

    @PutMapping("/{feedbackId}")
    public ResponseEntity<Feedback> updateFeedback(@PathVariable Long feedbackId, @Valid @RequestBody Feedback feedback) {
        log.info("Updating feedback with ID: {}", feedbackId);
        Feedback updated = feedbackService.updateFeedback(feedbackId, feedback);
        log.debug("Updated feedback with ID: {}", updated.getFeedbackId());
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{feedbackId}")
    public ResponseEntity<Feedback> patchFeedback(@PathVariable Long feedbackId, @RequestBody Feedback feedback) {
        log.info("Patching feedback with ID: {}", feedbackId);
        Feedback patched = feedbackService.patchFeedback(feedbackId, feedback);
        log.debug("Patched feedback with ID: {}", patched.getFeedbackId());
        return ResponseEntity.ok(patched);
    }

    @DeleteMapping("/{feedbackId}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable Long feedbackId) {
        log.info("Deleting feedback with ID: {}", feedbackId);
        feedbackService.deleteFeedback(feedbackId);
        log.info("Deleted feedback with ID: {}", feedbackId);
        return ResponseEntity.ok().build();
    }
}
