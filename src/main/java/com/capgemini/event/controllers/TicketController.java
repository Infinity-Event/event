package com.capgemini.event.controllers;

import java.util.List;

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

	private final TicketService ticketService;

	@Autowired
	public TicketController(TicketService ticketService) {
		this.ticketService = ticketService;
	}

	@GetMapping
	public ResponseEntity<List<Ticket>> findAllTickets() {
		return ResponseEntity.status(HttpStatus.OK).body(ticketService.getAllTickets());
	}

	@GetMapping("/{ticketId}")
	public ResponseEntity<Ticket> findTicketById(@PathVariable Long ticketId) {
		return ResponseEntity.status(HttpStatus.OK).body(ticketService.getTicketById(ticketId));
	}

	@PostMapping
	public ResponseEntity<Ticket> createTicket(@Valid @RequestBody Ticket ticket, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new IllegalArgumentException("Invalid Data");
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(ticketService.createTicket(ticket));
	}

	@PutMapping("/{ticketId}")
	public ResponseEntity<Ticket> updateTicket(@PathVariable Long ticketId, @Valid @RequestBody Ticket ticket) {
		return ResponseEntity.status(HttpStatus.OK).body(ticketService.updateTicket(ticketId, ticket));
	}

//	@PatchMapping("/{ticketId}")
//	public ResponseEntity<Ticket> patchTicket(@PathVariable Long ticketId, @RequestBody Ticket ticket) {
//		return ResponseEntity.status(HttpStatus.OK).body(ticketService.patchTicket(ticketId, ticket));
//	}

	@DeleteMapping("/{ticketId}")
	public ResponseEntity<Void> deleteTicket(@PathVariable Long ticketId) {
		ticketService.deleteTicket(ticketId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
