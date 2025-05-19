package com.capgemini.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import com.capgemini.event.controllers.TicketController;
import com.capgemini.event.entities.Event;
import com.capgemini.event.entities.Ticket;
import com.capgemini.event.entities.User;
import com.capgemini.event.services.TicketService;

@ExtendWith(MockitoExtension.class)
class TicketControllerTest {

	@Mock
	private TicketService ticketService;

	@InjectMocks
	private TicketController ticketController;

	@Test
	void findAllTickets() {
		Ticket ticket1 = new Ticket();
		Ticket ticket2 = new Ticket();
		List<Ticket> expectedTickets = Arrays.asList(ticket1, ticket2);
		when(ticketService.getAllTickets()).thenReturn(expectedTickets);

		ResponseEntity<List<Ticket>> response = ticketController.findAllTickets();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expectedTickets, response.getBody());
		verify(ticketService).getAllTickets();
	}

	@Test
	void findTicketById() {
		Long ticketId = 1L;
		Ticket expectedTicket = new Ticket();
		when(ticketService.getTicketById(ticketId)).thenReturn(expectedTicket);

		ResponseEntity<Ticket> response = ticketController.findTicketById(ticketId);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expectedTicket, response.getBody());
		verify(ticketService).getTicketById(ticketId);
	}

	@Test
	void createTicket() {
		Ticket input = new Ticket();
		Ticket created = new Ticket();
		when(ticketService.createTicket(input)).thenReturn(created);

		BindingResult bindingResult = mock(BindingResult.class);
		when(bindingResult.hasErrors()).thenReturn(false);

		ResponseEntity<Ticket> response = ticketController.createTicket(input, bindingResult);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(created, response.getBody());
		verify(ticketService).createTicket(input);
	}

	@Test
	void updateTicket() {
		Long ticketId = 1L;
		User user = new User();
		Event event = new Event();

		Ticket input = new Ticket(ticketId, LocalDate.now(), user, event);
		Ticket updated = new Ticket(ticketId, LocalDate.now().plusDays(1), user, event);

		when(ticketService.updateTicket(ticketId, input)).thenReturn(updated);

		ResponseEntity<Ticket> response = ticketController.updateTicket(ticketId, input);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(updated, response.getBody());
		verify(ticketService).updateTicket(ticketId, input);
	}

	@Test
	void patchTicket() {
		Long ticketId = 1L;
		Ticket patch = new Ticket();
		Ticket result = new Ticket();

		when(ticketService.patchTicket(ticketId, patch)).thenReturn(result);

		ResponseEntity<Ticket> response = ticketController.patchTicket(ticketId, patch);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(result, response.getBody());
		verify(ticketService).patchTicket(ticketId, patch);
	}

	@Test
	void deleteTicket() {
		Long ticketId = 1L;

		ResponseEntity<Void> response = ticketController.deleteTicket(ticketId);

		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
		verify(ticketService).deleteTicket(ticketId);
	}

}
