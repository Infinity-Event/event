package com.capgemini.event;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.capgemini.event.entities.Event;
import com.capgemini.event.entities.Ticket;
import com.capgemini.event.entities.User;
import com.capgemini.event.repositories.TicketRepo;
import com.capgemini.event.services.TicketServiceImpl;

@ExtendWith(MockitoExtension.class)
class TicketServiceImplTest {
	@Mock
	private TicketRepo ticketRepo;

	@InjectMocks
	private TicketServiceImpl ticketService;

	@Test
	void testGetAllTickets() {
		List<Ticket> mockTickets = Arrays.asList(new Ticket(1L, /* set fields */ null, null, null),
				new Ticket(2L, null, null, null));

		when(ticketRepo.findAll()).thenReturn(mockTickets);

		List<Ticket> tickets = ticketService.getAllTickets();

		assertEquals(2, tickets.size());
		verify(ticketRepo, times(1)).findAll();
	}

	@Test
	void testGetTicketById() {
		Ticket mockTicket = new Ticket(1L, null, null, null);
		when(ticketRepo.findById(1L)).thenReturn(Optional.of(mockTicket));

		Ticket ticket = ticketService.getTicketById(1L);

		assertNotNull(ticket);
		assertEquals(1L, ticket.getTicketId());
		verify(ticketRepo).findById(1L);
	}

	@Test
	void createTicket() {
		User user = new User();
		Event event = new Event();
		Ticket ticket = new Ticket(1L, LocalDate.now(), user, event);
	    when(ticketRepo.save(ticket)).thenReturn(ticket);

	    Ticket result = ticketService.createTicket(ticket);

	    assertEquals(ticket, result);
	    verify(ticketRepo).save(ticket);
	}


	@Test
	void updateTicket() {
		User user = new User();
		Event event = new Event();
		Ticket ticket = new Ticket(1L, LocalDate.now(), user, event);
		Ticket updatedTicket = new Ticket(1L, LocalDate.now().plusDays(1), user, event);

		when(ticketRepo.findById(1L)).thenReturn(Optional.of(ticket));
		when(ticketRepo.save(any(Ticket.class))).thenReturn(updatedTicket);

		Ticket result = ticketService.updateTicket(1L, updatedTicket);

		assertEquals(updatedTicket.getDate(), result.getDate());
		verify(ticketRepo).save(ticket);
	}

	@Test
	void patchTicket() {
		User user = new User();
		Event event = new Event();
		Ticket ticket = new Ticket(1L, LocalDate.now(), user, event);
		Ticket patch = new Ticket(LocalDate.now().plusDays(5), null, null);

		when(ticketRepo.findById(1L)).thenReturn(Optional.of(ticket));
		when(ticketRepo.save(any(Ticket.class))).thenReturn(ticket);

		Ticket result = ticketService.patchTicket(1L, patch);

		assertEquals(patch.getDate(), result.getDate());
		verify(ticketRepo).save(ticket);
	}

	void deleteTicket() {
		User user = new User();
		Event event = new Event();
		Ticket ticket = new Ticket(1L, LocalDate.now(), user, event);
		when(ticketRepo.findById(1L)).thenReturn(Optional.of(ticket));

		ticketService.deleteTicket(1L);

		verify(ticketRepo).delete(ticket);
	}

}