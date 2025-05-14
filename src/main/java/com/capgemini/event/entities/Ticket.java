package com.capgemini.event.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "ticket")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticketId")
    private Long ticketId;

    @NotNull(message = "Date is required")
    @FutureOrPresent(message = "Date must be today or in the future")
    @Column(name = "date")
    private LocalDate date;

    @NotNull(message = "User cannot be null")
    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @NotNull(message = "Event cannot be null")
    @ManyToOne
    @JoinColumn(name = "eventId", nullable = false)
    private Event event;

    public Ticket() {
    }

    public Ticket(Long ticketId, LocalDate date, User user, Event event) {
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
