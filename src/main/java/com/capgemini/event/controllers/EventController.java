package com.capgemini.event.controllers;

import com.capgemini.event.entities.Event;
import com.capgemini.event.services.EventService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@Slf4j
public class EventController {

    private EventService eventService;
    
    @Autowired
    public EventController(EventService eventService) {
    	this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        log.info("Received request to get all events");
        List<Event> events = eventService.getAllEvents();
        log.debug("Returning {} events", events.size());
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<Event> getEventById(@PathVariable Long eventId) {
        log.info("Received request to get event by ID: {}", eventId);
        Event event = eventService.getEventById(eventId);
        log.debug("Returning event with ID: {}", eventId);
        return ResponseEntity.ok(event);
    }

    @PostMapping("/users/{organizerId}")
    public ResponseEntity<Event> createEvent(@Valid @RequestBody Event event, @PathVariable Long organizerId ) {
    	if (organizerId == null) {
            throw new IllegalArgumentException("Organizer ID cannot be null");
        }
        log.info("Received request to create event: {}", event.getTitle());
        Event createdEvent = eventService.createEvent(event, organizerId);
        log.debug("Successfully created event with ID: {}", createdEvent.getEventId());
        return ResponseEntity.created(URI.create("/api/events/" + createdEvent.getEventId())).body(createdEvent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @Valid @RequestBody Event eventDetails) {
        log.info("Attempting to update event ID: {} with details: {}", id, eventDetails.getTitle());
        Event updatedEvent = eventService.updateEvent(id, eventDetails);
        log.debug("Successfully updated event with ID: {}", updatedEvent.getEventId());
        return ResponseEntity.ok(updatedEvent);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Event> patchEvent(@PathVariable Long id, @RequestBody Event partialEventDetails) {
        log.info("Received request to patch event with ID: {}", id);
        Event patchedEvent = eventService.patchEvent(id, partialEventDetails); 
        log.debug("Successfully patched event with ID: {}", patchedEvent.getEventId());
        return ResponseEntity.ok(patchedEvent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        log.info("Received request to delete event with ID: {}", id);
        eventService.deleteEvent(id); 
        log.debug("Event with ID {} successfully deleted", id);
        return ResponseEntity.noContent().build(); 
    }
}