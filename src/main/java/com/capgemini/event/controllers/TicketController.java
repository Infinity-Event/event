package com.capgemini.event.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.capgemini.event.entities.Ticket;
import com.capgemini.event.services.TicketService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

	private static final Logger logger = LoggerFactory.getLogger(TicketController.class);

	private final TicketService ticketService;

	@Autowired
	public TicketController(TicketService ticketService) {
		this.ticketService = ticketService;
	}

	@GetMapping
	public ResponseEntity<List<Ticket>> findAllTickets() {
		logger.info("GET /api/tickets - Fetching all tickets");
		List<Ticket> tickets = ticketService.getAllTickets();
		logger.debug("Found {} tickets", tickets.size());
		return ResponseEntity.status(HttpStatus.OK).body(tickets);
	}

	@GetMapping("/{ticketId}")
	public ResponseEntity<Ticket> findTicketById(@PathVariable Long ticketId) {
		logger.info("GET /api/tickets/{} - Fetching ticket by ID", ticketId);
		Ticket ticket = ticketService.getTicketById(ticketId);
		logger.debug("Fetched ticket: {}", ticket);
		return ResponseEntity.status(HttpStatus.OK).body(ticket);
	}

	@PostMapping
	public ResponseEntity<Ticket> createTicket(@Valid @RequestBody Ticket ticket, BindingResult bindingResult) {
		logger.info("POST /api/tickets - Creating new ticket");
		if (bindingResult.hasErrors()) {
			logger.error("Validation failed for ticket: {}", bindingResult.getFieldErrors());
			throw new IllegalArgumentException(bindingResult.getFieldErrors().toString());
		}
		Ticket created = ticketService.createTicket(ticket);
		logger.debug("Created ticket: {}", created);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	@PutMapping("/{ticketId}")
	public ResponseEntity<Ticket> updateTicket(@PathVariable Long ticketId, @Valid @RequestBody Ticket ticket) {
		logger.info("PUT /api/tickets/{} - Updating ticket", ticketId);
		Ticket updated = ticketService.updateTicket(ticketId, ticket);
		logger.debug("Updated ticket: {}", updated);
		return ResponseEntity.status(HttpStatus.OK).body(updated);
	}

	@PatchMapping("/{ticketId}")
	public ResponseEntity<Ticket> patchTicket(@PathVariable Long ticketId, @RequestBody Ticket ticket) {
		logger.info("PATCH /api/tickets/{} - Patching ticket", ticketId);
		Ticket patched = ticketService.patchTicket(ticketId, ticket);
		logger.debug("Patched ticket: {}", patched);
		return ResponseEntity.status(HttpStatus.OK).body(patched);
	}

	@DeleteMapping("/{ticketId}")
	public ResponseEntity<Void> deleteTicket(@PathVariable Long ticketId) {
		logger.info("DELETE /api/tickets/{} - Deleting ticket", ticketId);
		ticketService.deleteTicket(ticketId);
		logger.debug("Deleted ticket with ID: {}", ticketId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}