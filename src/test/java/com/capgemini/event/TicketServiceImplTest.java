package com.capgemini.event;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.capgemini.event.entities.Ticket;
import com.capgemini.event.repositories.TicketRepo;
import com.capgemini.event.services.TicketServiceImpl;

@ExtendWith(MockitoExtension.class)
public class TicketServiceImplTest {
	@Mock
	private TicketRepo ticketRepo;

	@InjectMocks
	private TicketServiceImpl ticketService;

	@Test
	public void testGetAllTickets() {
		List<Ticket> mockTickets = Arrays.asList(new Ticket(1L, /* set fields */ null, null, null),
				new Ticket(2L, null, null, null));

		when(ticketRepo.findAll()).thenReturn(mockTickets);

		List<Ticket> tickets = ticketService.getAllTickets();

		assertEquals(2, tickets.size());
		verify(ticketRepo, times(1)).findAll();
	}

	@Test
	public void testGetTicketById() {
		Ticket mockTicket = new Ticket(1L, null, null, null);
		when(ticketRepo.findById(1L)).thenReturn(Optional.of(mockTicket));

		Ticket ticket = ticketService.getTicketById(1L);

		assertNotNull(ticket);
		assertEquals(1L, ticket.getTicketId());
		verify(ticketRepo).findById(1L);
	}

	@Test
	public void testCreateTicket() {
		Ticket newTicket = new Ticket(null, null, null, null);
		Ticket savedTicket = new Ticket(1L, null, null, null);

		when(ticketRepo.save(newTicket)).thenReturn(savedTicket);

		Ticket result = ticketService.createTicket(newTicket);

		assertNotNull(result);
		assertEquals(1L, result.getTicketId());
		verify(ticketRepo).save(newTicket);
	}

}