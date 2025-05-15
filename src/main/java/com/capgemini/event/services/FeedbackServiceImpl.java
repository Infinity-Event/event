package com.capgemini.event.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.event.entities.Event;
import com.capgemini.event.entities.Feedback;
import com.capgemini.event.entities.User;
import com.capgemini.event.repositories.EventRepo;
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
		if (feedback.getUser() == null || feedback.getUser().getUserId() == null) {
			throw new IllegalArgumentException("Feedback must be associated with a valid User.");
		}
		if (feedback.getEvent() == null || feedback.getEvent().getEventId() == null) {
			throw new IllegalArgumentException("Feedback must be associated with a valid Event.");
		}

		User user = userRepo.findById(feedback.getUser().getUserId()).orElseThrow(
				() -> new EntityNotFoundException("User not found with id: " + feedback.getUser().getUserId()));
		Event event = eventRepo.findById(feedback.getEvent().getEventId()).orElseThrow(
				() -> new EntityNotFoundException("Event not found with id: " + feedback.getEvent().getEventId()));

		feedback.setUser(user);
		feedback.setEvent(event);

		return feedbackRepo.save(feedback);
	}

	@Override
	public Feedback updateFeedback(Long id, Feedback updatedFeedback) {
		Feedback existing = getFeedbackById(id);

		if (updatedFeedback.getUser() == null || updatedFeedback.getUser().getUserId() == null) {
			throw new IllegalArgumentException("User is required for feedback update.");
		}
		if (updatedFeedback.getEvent() == null || updatedFeedback.getEvent().getEventId() == null) {
			throw new IllegalArgumentException("Event is required for feedback update.");
		}

		User user = userRepo.findById(updatedFeedback.getUser().getUserId()).orElseThrow(
				() -> new EntityNotFoundException("User not found with id: " + updatedFeedback.getUser().getUserId()));
		Event event = eventRepo.findById(updatedFeedback.getEvent().getEventId())
				.orElseThrow(() -> new EntityNotFoundException(
						"Event not found with id: " + updatedFeedback.getEvent().getEventId()));

		existing.setRating(updatedFeedback.getRating());
		existing.setReview(updatedFeedback.getReview());
		existing.setUser(user);
		existing.setEvent(event);

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

		if (partialFeedback.getUser() != null && partialFeedback.getUser().getUserId() != null) {
			User user = userRepo.findById(partialFeedback.getUser().getUserId())
					.orElseThrow(() -> new EntityNotFoundException(
							"User not found with id: " + partialFeedback.getUser().getUserId()));
			existing.setUser(user);
		}

		if (partialFeedback.getEvent() != null && partialFeedback.getEvent().getEventId() != null) {
			Event event = eventRepo.findById(partialFeedback.getEvent().getEventId())
					.orElseThrow(() -> new EntityNotFoundException(
							"Event not found with id: " + partialFeedback.getEvent().getEventId()));
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
