package com.capgemini.event.repositories;

import com.capgemini.event.entities.Event;
import com.capgemini.event.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface EventRepo extends JpaRepository<Event, Long> {

    List<Event> findByOrganizer(User organizer);

}