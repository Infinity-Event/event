package com.capgemini.event.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.event.entities.Ticket;
import com.capgemini.event.exceptions.TicketNotFoundException;
import com.capgemini.event.repositories.TicketRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TicketServiceImpl implements TicketService {

	TicketRepo ticketRepo;

	@Autowired
	public TicketServiceImpl(TicketRepo ticketRepo) {
		this.ticketRepo = ticketRepo;
	}

	@Override
	public List<Ticket> getAllTickets() {
		log.info("Fetching all tickets");
		List<Ticket> tickets = ticketRepo.findAll();
		log.debug("Found {} tickets", tickets.size());
		return tickets;
	}

	@Override
	public Ticket getTicketById(Long ticketId) {
		log.info("Fetching ticket with ID: {}", ticketId);
		return ticketRepo.findById(ticketId).orElseThrow(() -> {
			log.error("Ticket with ID {} not found", ticketId);
			return new TicketNotFoundException("Ticket Not found");
		});
	}

	@Override
	public Ticket createTicket(Ticket ticket) {
		log.info("Creating new ticket: {}", ticket);
		Ticket saved = ticketRepo.save(ticket);
		log.debug("Created ticket with ID: {}", saved.getTicketId());
		return saved;
	}

	@Override
	public Ticket updateTicket(Long ticketId, Ticket updatedTicket) {
		log.info("Updating ticket with ID: {}", ticketId);

		return ticketRepo.findById(ticketId).map(ticket -> {
			ticket.setDate(updatedTicket.getDate());
			ticket.setEvent(updatedTicket.getEvent());
			ticket.setUser(updatedTicket.getUser());
			return ticketRepo.save(ticket);
		}).orElseThrow(() -> {
			log.warn("Ticket not found for update with ID: {}", ticketId);
			return new TicketNotFoundException("Ticket with ID " + ticketId + " not found");
		});
	}

	@Override
	public Ticket patchTicket(Long ticketId, Ticket patchedTicket) {
		log.info("Patching ticket with ID: {}", ticketId);

		Ticket existingTicket = ticketRepo.findById(ticketId)
				.orElseThrow(() -> new TicketNotFoundException("Ticket not found to patch"));

		if (patchedTicket.getDate() != null) {
			existingTicket.setDate(patchedTicket.getDate());
		}
		if (patchedTicket.getEvent() != null) {
			existingTicket.setEvent(patchedTicket.getEvent());
		}
		if (patchedTicket.getUser() != null) {
			existingTicket.setUser(patchedTicket.getUser());
		}

		Ticket patched = ticketRepo.save(existingTicket);
		log.debug("Patched ticket: {}", patched);
		return patched;
	}

	@Override
	public void deleteTicket(Long ticketId) {
		log.info("Deleting ticket with ID: {}", ticketId);
		Ticket ticket = ticketRepo.findById(ticketId).orElseThrow(() -> {
			log.error("Ticket with ID {} not found for deletion", ticketId);
			return new TicketNotFoundException("Ticket not found ID: " + ticketId);
		});
		ticketRepo.delete(ticket);
		log.debug("Deleted ticket with ID: {}", ticketId);
	}

}
