package com.capgemini.event.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.event.entities.Ticket;
import com.capgemini.event.repositories.TicketRepo;

@Service
public class TicketServiceImpl implements TicketService {

	TicketRepo ticketRepo;

	@Autowired
	public TicketServiceImpl(TicketRepo ticketRepo) {
		this.ticketRepo = ticketRepo;
	}

	@Override
	public List<Ticket> getAllTickets() {
		return ticketRepo.findAll();
	}

	@Override
	public Ticket getTicketById(Long ticketId) {
		return ticketRepo.findById(ticketId)
				.orElseThrow(() -> new RuntimeException("Ticket \"" + ticketId + "\"Not found"));
	}

	@Override
	public Ticket createTicket(Ticket ticket) {
		return ticketRepo.save(ticket);
	}

	@Override
	public Ticket updateTicket(Long ticketId, Ticket updatedTicket) {
		Optional<Ticket> optiTicket = ticketRepo.findById(ticketId);

		if (optiTicket.isPresent()) {
			Ticket existingTicket = optiTicket.get();
			existingTicket.setDate(updatedTicket.getDate());
			existingTicket.setEvent(updatedTicket.getEvent());
			existingTicket.setUser(updatedTicket.getUser());
			return ticketRepo.save(existingTicket);
		}
		return null;
	}

	@Override
	public Ticket patchTicket(Long ticketId, Ticket patchedTicket) {
		Optional<Ticket> optiTicket = ticketRepo.findById(ticketId);

		if (optiTicket.isPresent()) {
			Ticket existingTicket = optiTicket.get();
			if(existingTicket.getDate()!=null) {
				existingTicket.setDate(patchedTicket.getDate());
			}
			if(existingTicket.getEvent()!=null) {
				existingTicket.setEvent(patchedTicket.getEvent());
			}
			if(existingTicket.getUser()!=null) {
				existingTicket.setUser(patchedTicket.getUser());
			}
			return ticketRepo.save(existingTicket);
		}
		return null;
	}

	@Override
	public void deleteTicket(Long ticketId) {
		Ticket ticket = ticketRepo.findById(ticketId).orElseThrow(()-> new RuntimeException("Ticket not found ID : "+ticketId));
		ticketRepo.delete(ticket);
	}

}
