package com.capgemini.event.services;

import java.util.List;

import com.capgemini.event.entities.Ticket;

public interface TicketService {
	List<Ticket> getAllTickets();

	Ticket getTicketById(Long ticketId);

	Ticket createTicket(Ticket ticket, Long eventId, Long userId);

	Ticket updateTicket(Long ticketId, Ticket ticket);

	Ticket patchTicket(Long ticketId, Ticket ticket);

	void deleteTicket(Long ticketId);
}
