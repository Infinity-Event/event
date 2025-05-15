package com.capgemini.event.controllers;

import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.event.entities.Feedback;
import com.capgemini.event.services.FeedbackService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/feedbacks")
public class FeedbackRestController {

	private static final Logger logger = LoggerFactory.getLogger(FeedbackRestController.class);

	private final FeedbackService feedbackService;

	@Autowired
	public FeedbackRestController(FeedbackService feedbackService) {
		this.feedbackService = feedbackService;
	}

	@GetMapping
	public ResponseEntity<List<Feedback>> getAllFeedbacks() {
		logger.info("Fetching all feedbacks");
		List<Feedback> feedbacks = feedbackService.getAllFeedbacks();
		logger.debug("Total feedbacks found: {}", feedbacks.size());
		return ResponseEntity.ok(feedbacks);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Feedback> getFeedbackById(@PathVariable Long id) {
		logger.info("Fetching feedback with ID: {}", id);
		Feedback feedback = feedbackService.getFeedbackById(id);
		return ResponseEntity.ok(feedback);
	}

	@PostMapping
	public ResponseEntity<Feedback> createFeedback(@Valid @RequestBody Feedback feedback, BindingResult bindingResult) {
		logger.info("Creating feedback: {}", feedback);
		if (bindingResult.hasErrors()) {

			logger.warn("Invalid feedback data: {}", bindingResult.getAllErrors());
			throw new IllegalArgumentException("Invalid Data");

		}
		Feedback created = feedbackService.createFeedback(feedback);
		logger.info("Created feedback with ID: {}", created.getFeedbackId());
		return ResponseEntity.created(URI.create("/api/feedbacks/" + created.getFeedbackId())).body(created);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Feedback> updateFeedback(@PathVariable Long id, @Valid @RequestBody Feedback feedback) {
		logger.info("Updating feedback with ID: {}", id);
		Feedback updated = feedbackService.updateFeedback(id, feedback);
		logger.info("Updated feedback with ID: {}", updated.getFeedbackId());
		return ResponseEntity.ok(updated);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Feedback> patchFeedback(@PathVariable Long id, @RequestBody Feedback feedback) {
		logger.info("Patching feedback with ID: {}", id);
		Feedback patched = feedbackService.patchFeedback(id, feedback);
		logger.info("Patched feedback with ID: {}", patched.getFeedbackId());
		return ResponseEntity.ok(patched);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteFeedback(@PathVariable Long id) {
		logger.info("Deleting feedback with ID: {}", id);
		feedbackService.deleteFeedback(id);
		logger.info("Deleted feedback with ID: {}", id);
		return ResponseEntity.ok().build();
	}
}
