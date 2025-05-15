package com.capgemini.event.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "registrations", uniqueConstraints = { @UniqueConstraint(columnNames = { "user_id", "event_id" }) })
public class Registration {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long regId;

	@NotNull(message = "Registration date is required")
	private LocalDate regDate;

	@Column(nullable = false)
	private String status; // ✅ New column

	@ManyToOne
	@NotNull(message = "User is required")
	private User user;

	@ManyToOne
	@NotNull(message = "Event is required")
	private Event event;

	public Registration() {
	}

	public Registration(Long regId, LocalDate regDate, String status, User user, Event event) {
		this.regId = regId;
		this.regDate = regDate;
		this.status = status;
		this.user = user;
		this.event = event;
	}

	// Getters and setters
	public Long getRegId() {
		return regId;
	}

	public void setRegId(Long regId) {
		this.regId = regId;
	}

	public LocalDate getRegDate() {
		return regDate;
	}

	public void setRegDate(LocalDate regDate) {
		this.regDate = regDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
		return "Registration [regId=" + regId + ", regDate=" + regDate + ", status=" + status + ", user=" + user
				+ ", event=" + event + "]";
	}

}