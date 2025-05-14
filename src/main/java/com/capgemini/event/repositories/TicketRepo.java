package com.capgemini.event.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capgemini.event.entities.Ticket;

public interface TicketRepo extends JpaRepository<Ticket, Long>{

}
