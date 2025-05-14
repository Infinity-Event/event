package com.capgemini.event.controllers;

import com.capgemini.event.entities.Event;
import com.capgemini.event.services.EventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/organizer/{organizerId}")
    public ResponseEntity<Event> createEvent(@PathVariable Long organizerId,
                                             @Valid @RequestBody Event event) {
        Event createdEvent = eventService.createEvent(event, organizerId);
        if (createdEvent == null) {
            return ResponseEntity.badRequest().build(); 
        }
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<Event> getEventById(@PathVariable Long eventId) {
        Event event = eventService.getEventById(eventId);
        if (event == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(event);
    }

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }
    
    @GetMapping("/organizer/{organizerId}")
    public ResponseEntity<List<Event>> getEventsByOrganizer(@PathVariable Long organizerId) {
        List<Event> events = eventService.getEventsByOrganizer(organizerId);
         if (events.isEmpty() && eventService.getEventsByOrganizer(organizerId) == null ) { // Check if organizer itself not found
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(events);
    }

    @GetMapping("/organizer/{organizerId}/upcoming")
    public ResponseEntity<List<Event>> getUpcomingEventsByOrganizer(@PathVariable Long organizerId) {
        List<Event> events = eventService.getUpcomingEventsByOrganizer(organizerId);
        return ResponseEntity.ok(events);
    }
    
    @GetMapping("/organizer/{organizerId}/past")
    public ResponseEntity<List<Event>> getPastEventsByOrganizer(@PathVariable Long organizerId) {
        List<Event> events = eventService.getPastEventsByOrganizer(organizerId);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/organizer/{organizerId}/ongoing")
    public ResponseEntity<List<Event>> getOngoingEventsByOrganizer(@PathVariable Long organizerId) {
        List<Event> events = eventService.getOngoingEventsByOrganizer(organizerId);
        return ResponseEntity.ok(events);
    }

    @PutMapping("/{eventId}/organizer/{currentUserId}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long eventId,
                                             @PathVariable Long currentUserId,
                                             @Valid @RequestBody Event eventDetails) {
        Event originalEvent = eventService.getEventById(eventId);
        if (originalEvent == null) {
            return ResponseEntity.notFound().build();
        }
        Event updatedEvent = eventService.updateEvent(eventId, eventDetails, currentUserId);
        if (updatedEvent == null) { 
            return ResponseEntity.notFound().build(); // Should ideally not happen if original existed, means user not found during update
        }
        if (Objects.equals(updatedEvent.getEventId(), originalEvent.getEventId()) && updatedEvent == originalEvent) {
             // Check if the event was returned unmodified by the service (signifying potential auth failure)
             // This check is a bit weak; depends on service impl for unauthorized update.
             return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(updatedEvent);
    }

    @DeleteMapping("/{eventId}/organizer/{currentUserId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId, @PathVariable Long currentUserId) {
        boolean deleted = eventService.deleteEvent(eventId, currentUserId);
        if (!deleted) {
            Event eventExists = eventService.getEventById(eventId);
            if(eventExists == null) return ResponseEntity.notFound().build(); // Event not found
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Found but not authorized to delete
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<Event>> getUpcomingEvents() {
        List<Event> events = eventService.getUpcomingEvents();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/past")
    public ResponseEntity<List<Event>> getPastEvents() {
        List<Event> events = eventService.getPastEvents();
        return ResponseEntity.ok(events);
    }
    
    @GetMapping("/trending")
    public ResponseEntity<List<Event>> getTrendingEvents() {
        List<Event> events = eventService.getTrendingEvents();
        return ResponseEntity.ok(events);
    }
}