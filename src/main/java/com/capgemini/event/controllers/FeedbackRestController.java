package com.capgemini.event.controllers;

import com.capgemini.event.entities.Feedback;
import com.capgemini.event.services.FeedbackService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/feedbacks")
public class FeedbackRestController {

	private final FeedbackService feedbackService;

	@Autowired
	public FeedbackRestController(FeedbackService feedbackService) {
		this.feedbackService = feedbackService;
	}

	@GetMapping
	public ResponseEntity<List<Feedback>> getAllFeedbacks() {
		return ResponseEntity.ok(feedbackService.getAllFeedbacks());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Feedback> getFeedbackById(@PathVariable Long id) {
		return ResponseEntity.ok(feedbackService.getFeedbackById(id));
	}

	@PostMapping
	public ResponseEntity<Feedback> createFeedback(@Valid @RequestBody Feedback feedback, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new IllegalArgumentException("Invalid Data");
		}
		Feedback created = feedbackService.createFeedback(feedback);
		return ResponseEntity.created(URI.create("/api/feedbacks/" + created.getFeedbackId())).body(created);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Feedback> updateFeedback(@PathVariable Long id, @Valid @RequestBody Feedback feedback) {
		return ResponseEntity.ok(feedbackService.updateFeedback(id, feedback));
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Feedback> patchFeedback(@PathVariable Long id, @RequestBody Feedback feedback) {
		return ResponseEntity.ok(feedbackService.patchFeedback(id, feedback));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteFeedback(@PathVariable Long id) {
		feedbackService.deleteFeedback(id);
		return ResponseEntity.ok().build();
	}
}
