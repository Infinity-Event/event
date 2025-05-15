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

	@Min(value = 1, message = "Rating must be at least 1")
	@Max(value = 5, message = "Rating must be at most 5")
	@Column(name = "rating")
	private int rating;

	@NotBlank(message = "Review cannot be blank")
	@Size(min = 10, max = 1000, message = "Review must be between 10 and 1000 characters")
	@Column(name = "review")
	private String review;

	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(optional = false)
	@JoinColumn(name = "event_id")
	private Event event;

	public Feedback() {
		
	}

	public Feedback(Long feedbackId, int rating, String review, User user, Event event) {
		this.feedbackId = feedbackId;
		this.rating = rating;
		this.review = review;
		this.user = user;
		this.event = event;
	}


	public Long getFeedbackId() {
		return feedbackId;
	}

	public void setFeedbackId(Long feedbackId) {
		this.feedbackId = feedbackId;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
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
		return "Feedback [feedbackId=" + feedbackId + ", rating=" + rating + ", review=" + review
				+ ", user=" + user + ", event=" + event + "]";
	}
}
