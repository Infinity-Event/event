package com.capgemini.event.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.event.entities.Ticket;
import com.capgemini.event.services.TicketService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/tickets")
@Slf4j
public class TicketController {

	private final TicketService ticketService;

	@Autowired
	public TicketController(TicketService ticketService) {
		this.ticketService = ticketService;
	}

	@GetMapping
	public ResponseEntity<List<Ticket>> findAllTickets() {
		log.info("GET /api/tickets - Fetching all tickets");
		List<Ticket> tickets = ticketService.getAllTickets();
		log.debug("Found {} tickets", tickets.size());
		return ResponseEntity.status(HttpStatus.OK).body(tickets);
	}

	@GetMapping("/{ticketId}")
	public ResponseEntity<Ticket> findTicketById(@PathVariable Long ticketId) {
		log.info("GET /api/tickets/{} - Fetching ticket by ID", ticketId);
		Ticket ticket = ticketService.getTicketById(ticketId);
		log.debug("Fetched ticket: {}", ticket);
		return ResponseEntity.status(HttpStatus.OK).body(ticket);
	}

	@PostMapping("/events/{eventId}/users/{userId}")
	public ResponseEntity<Ticket> createTicket(@Valid @RequestBody Ticket ticket, @PathVariable Long eventId,
			@PathVariable Long userId, BindingResult bindingResult) {
		log.info("POST /api/tickets - Creating new ticket");
		if (bindingResult.hasErrors()) {
			log.error("Validation failed for ticket: {}", bindingResult.getFieldErrors());
			throw new IllegalArgumentException(bindingResult.getFieldErrors().toString());
		}
		Ticket created = ticketService.createTicket(ticket, eventId, userId);
		log.debug("Created ticket: {}", created);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	@PutMapping("/{ticketId}")
	public ResponseEntity<Ticket> updateTicket(@PathVariable Long ticketId, @Valid @RequestBody Ticket ticket) {
		log.info("PUT /api/tickets/{} - Updating ticket", ticketId);
		Ticket updated = ticketService.updateTicket(ticketId, ticket);
		log.debug("Updated ticket: {}", updated);
		return ResponseEntity.status(HttpStatus.OK).body(updated);
	}

	@PatchMapping("/{ticketId}")
	public ResponseEntity<Ticket> patchTicket(@PathVariable Long ticketId, @RequestBody Ticket ticket) {
		log.info("PATCH /api/tickets/{} - Patching ticket", ticketId);
		Ticket patched = ticketService.patchTicket(ticketId, ticket);
		log.debug("Patched ticket: {}", patched);
		return ResponseEntity.status(HttpStatus.OK).body(patched);
	}

	@DeleteMapping("/{ticketId}")
	public ResponseEntity<Void> deleteTicket(@PathVariable Long ticketId) {
		log.info("DELETE /api/tickets/{} - Deleting ticket", ticketId);
		ticketService.deleteTicket(ticketId);
		log.debug("Deleted ticket with ID: {}", ticketId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
