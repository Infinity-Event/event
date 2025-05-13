package com.capgemini.event.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Ticket {

	@Id
	@GeneratedValue
	private Long ticketId;
	private LocalDate date;

	@ManyToOne
	private User user;
	@ManyToOne
	private Event event;

	public Ticket() {

	}

	public Ticket(Long ticketId, LocalDate date, User user, Event event) {
		super();
		this.ticketId = ticketId;
		this.date = date;
		this.user = user;
		this.event = event;
	}

	public Long getTicketId() {
		return ticketId;
	}

	public void setTicketId(Long ticketId) {
		this.ticketId = ticketId;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
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
		return "Ticket [ticketId=" + ticketId + ", date=" + date + ", user=" + user + ", event=" + event + "]";
	}

}
