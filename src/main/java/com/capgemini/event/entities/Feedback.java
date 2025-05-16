package com.capgemini.event.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "feedbacks")
public class Feedback {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "feedback_id")
	private Long feedbackId;

	@NotNull(message = "Rating is required")
	@Min(value = 1, message = "Rating must be at least 1")
	@Max(value = 5, message = "Rating must be at most 5")
	@Column(name = "rating")
	private Integer rating;

	@NotBlank(message = "Review cannot be blank")
	@Size(min = 4, max = 1000, message = "Review must be between 4 and 1000 characters")
	@Column(name = "review")
	private String review;

	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(optional = false)
	@JoinColumn(name = "event_id", nullable = false)
	private Event event;

	public Feedback() {
		
	}
	public Feedback(Long feedbackId, Integer rating, String review, User user, Event event) {
		this.feedbackId = feedbackId;
		this.rating = rating;
		this.review = review;
		this.user = user;
		this.event = event;
	}
	public Feedback(Integer rating, String review, User user, Event event) {
		this.rating = rating;
		this.review = review;
		this.user = user;
		this.event = event;
	}

	public Feedback(Integer rating, String review) {
		this.rating = rating;
		this.review = review;
	}


	public Long getFeedbackId() {
		return feedbackId;
	}

	public void setFeedbackId(Long feedbackId) {
		this.feedbackId = feedbackId;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	@Override
	public String toString() {
	    return "Feedback [feedbackId=" + feedbackId + ", rating=" + rating + ", review=" + review + "]";
	}
}
