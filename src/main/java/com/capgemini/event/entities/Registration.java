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

@Entity
@Table(name = "registrations", uniqueConstraints = { @UniqueConstraint(columnNames = { "user_id", "event_id" }) })
public class Registration {

	@Id
	@GeneratedValue
	private Long regId;
	private LocalDate regDate;

	@ManyToOne
	private User user;
	@ManyToOne
	private Event event;

	public Registration() {

	}

	public Registration(Long regId, LocalDate regDate, User user, Event event) {
		super();
		this.regId = regId;
		this.regDate = regDate;
		this.user = user;
		this.event = event;
	}

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
		return "Registration [regId=" + regId + ", regDate=" + regDate + ", user=" + user + ", event=" + event + "]";
	}

}