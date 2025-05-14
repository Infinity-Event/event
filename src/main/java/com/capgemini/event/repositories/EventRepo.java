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

    @Query("SELECT e FROM Event e WHERE e.organizer = :organizer AND (e.date > :currentDate OR (e.date = :currentDate AND e.time > :currentTime)) ORDER BY e.date ASC, e.time ASC")
    List<Event> findUpcomingByOrganizer(@Param("organizer") User organizer, @Param("currentDate") LocalDate currentDate, @Param("currentTime") LocalTime currentTime);

    @Query("SELECT e FROM Event e WHERE e.organizer = :organizer AND (e.date < :currentDate OR (e.date = :currentDate AND e.time <= :currentTime)) ORDER BY e.date DESC, e.time DESC")
    List<Event> findPastByOrganizer(@Param("organizer") User organizer, @Param("currentDate") LocalDate currentDate, @Param("currentTime") LocalTime currentTime);
    
    List<Event> findByOrganizerAndDateOrderByTimeAsc(User organizer, LocalDate date);


    @Query("SELECT e FROM Event e WHERE e.date > :currentDate OR (e.date = :currentDate AND e.time > :currentTime) ORDER BY e.date ASC, e.time ASC")
    List<Event> findAllUpcoming(@Param("currentDate") LocalDate currentDate, @Param("currentTime") LocalTime currentTime);

    @Query("SELECT e FROM Event e WHERE e.date < :currentDate OR (e.date = :currentDate AND e.time <= :currentTime) ORDER BY e.date DESC, e.time DESC")
    List<Event> findAllPast(@Param("currentDate") LocalDate currentDate, @Param("currentTime") LocalTime currentTime);
    
    List<Event> findByDateOrderByTimeAsc(LocalDate date);
}