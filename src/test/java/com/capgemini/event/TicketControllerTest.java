package com.capgemini.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.capgemini.event.controllers.TicketController;
import com.capgemini.event.entities.Ticket;
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


}
