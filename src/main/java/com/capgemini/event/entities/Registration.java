package com.capgemini.event.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Table(name = "registrations", uniqueConstraints = { @UniqueConstraint(columnNames = { "user_id", "event_id" }) })
public class Registration {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "reg_id")
	private Long regId;

	@ManyToOne(fetch = FetchType.LAZY, optional =false)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY, optional=false)
	@JoinColumn(name = "event_id")
	private Event event;

	@NotNull(message = "Registration date must not be null")
	@Column(name = "reg_date")
	private LocalDate regDate;

	public Registration() {
	}

	public Registration(User user, Event event) {
		this.user = user;
		this.event = event;
	}

	public Long getRegId() {
		return regId;
	}

	public void setRegId(Long regId) {
		this.regId = regId;
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

	public LocalDate getRegDate() {
		return regDate;
	}

	public void setRegDate(LocalDate regDate) {
		this.regDate = regDate;
	}

	@Override
	public String toString() {
		return "Registration{" + "regId=" + regId + ", userId=" + (user != null ? user.getUserId() : null)
				+ ", eventId=" + (event != null ? event.getEventId() : null) + ", regDate=" + regDate + '}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Registration that = (Registration) o;
		return regId != null && regId.equals(that.regId);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}