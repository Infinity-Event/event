package com.capgemini.event.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.capgemini.event.entities.Ticket;
import com.capgemini.event.repositories.TicketRepo;

@Service
public class TicketServiceImpl implements TicketService {

	private static final Logger logger = LoggerFactory.getLogger(TicketServiceImpl.class);

	TicketRepo ticketRepo;

	@Autowired
	public TicketServiceImpl(TicketRepo ticketRepo) {
		this.ticketRepo = ticketRepo;
	}

	@Override
	public List<Ticket> getAllTickets() {
		logger.info("Fetching all tickets");
		List<Ticket> tickets = ticketRepo.findAll();
		logger.debug("Found {} tickets", tickets.size());
		return tickets;
	}

	@Override
	public Ticket getTicketById(Long ticketId) {
		logger.info("Fetching ticket with ID: {}", ticketId);
		return ticketRepo.findById(ticketId).orElseThrow(() -> {
			logger.error("Ticket with ID {} not found", ticketId);
			return new RuntimeException("Ticket \"" + ticketId + "\" Not found");
		});
	}

	@Override
	public Ticket createTicket(Ticket ticket) {
		logger.info("Creating new ticket: {}", ticket);
		Ticket saved = ticketRepo.save(ticket);
		logger.debug("Created ticket with ID: {}", saved.getTicketId());
		return saved;
	}

	@Override
	public Ticket updateTicket(Long ticketId, Ticket updatedTicket) {
		logger.info("Updating ticket with ID: {}", ticketId);
		Optional<Ticket> optiTicket = ticketRepo.findById(ticketId);

		if (optiTicket.isPresent()) {
			Ticket existingTicket = optiTicket.get();
			existingTicket.setDate(updatedTicket.getDate());
			existingTicket.setEvent(updatedTicket.getEvent());
			existingTicket.setUser(updatedTicket.getUser());
			Ticket updated = ticketRepo.save(existingTicket);
			logger.debug("Updated ticket: {}", updated);
			return updated;
		} else {
			logger.warn("Ticket with ID {} not found for update", ticketId);
			return null;
		}
	}

	@Override
	public Ticket patchTicket(Long ticketId, Ticket patchedTicket) {
		logger.info("Patching ticket with ID: {}", ticketId);
		Optional<Ticket> optiTicket = ticketRepo.findById(ticketId);

		if (optiTicket.isPresent()) {
			Ticket existingTicket = optiTicket.get();

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
			logger.debug("Patched ticket: {}", patched);
			return patched;
		} else {
			logger.warn("Ticket with ID {} not found for patching", ticketId);
			return null;
		}
	}

	@Override
	public void deleteTicket(Long ticketId) {
		logger.info("Deleting ticket with ID: {}", ticketId);
		Ticket ticket = ticketRepo.findById(ticketId).orElseThrow(() -> {
			logger.error("Ticket with ID {} not found for deletion", ticketId);
			return new RuntimeException("Ticket not found ID: " + ticketId);
		});
		ticketRepo.delete(ticket);
		logger.debug("Deleted ticket with ID: {}", ticketId);
	}

}
