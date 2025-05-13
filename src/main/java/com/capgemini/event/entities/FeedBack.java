package com.capgemini.event.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "feedbacks")
public class FeedBack {

	@Id
	@GeneratedValue
	private Long feedbackId;
	private int rating;
	private String review;

	@ManyToOne
	private User user;
	@ManyToOne
	private Event event;

	public FeedBack() {

	}

	public FeedBack(Long feedbackId, int rating, String review, User user, Event event) {
		super();
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
		return "FeedBack [feedbackId=" + feedbackId + ", rating=" + rating + ", review=" + review + ", user=" + user
				+ ", event=" + event + "]";
	}

}
