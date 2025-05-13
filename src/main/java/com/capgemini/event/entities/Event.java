package com.capgemini.event.entities;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "events")
public class Event {

	@Id
	@GeneratedValue
	private Long eventId;
	private Long organizerId;
	private String title;
	private LocalDate date;
	private LocalTime time;
	private String location;
	private Integer capacity;

	@Enumerated(EnumType.STRING)
	private Category type;

	@ManyToOne
	private User user;

	public Event() {

	}

	public Event(Long eventId, Long organizerId, String title, LocalDate date, LocalTime time, String location,
			Integer capacity, Category type, User user) {
		super();
		this.eventId = eventId;
		this.organizerId = organizerId;
		this.title = title;
		this.date = date;
		this.time = time;
		this.location = location;
		this.capacity = capacity;
		this.type = type;
		this.user = user;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public Long getOrganizerId() {
		return organizerId;
	}

	public void setOrganizerId(Long organizerId) {
		this.organizerId = organizerId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	public Category getType() {
		return type;
	}

	public void setType(Category type) {
		this.type = type;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Event [eventId=" + eventId + ", organizerId=" + organizerId + ", title=" + title + ", date=" + date
				+ ", time=" + time + ", location=" + location + ", capacity=" + capacity + ", type=" + type + ", user="
				+ user + "]";
	}

}