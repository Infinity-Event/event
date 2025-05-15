package com.capgemini.event.controllers;

import com.capgemini.event.entities.Event;
import com.capgemini.event.services.EventService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private static final Logger logger = LoggerFactory.getLogger(EventController.class);

    @Autowired
    private EventService eventService;

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        logger.info("Received request to get all events");
        List<Event> events = eventService.getAllEvents();
        logger.info("Returning {} events", events.size());
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        logger.info("Received request to get event by ID: {}", id);
        Event event = eventService.getEventById(id);
        if (event == null) {
            logger.warn("Event not found for ID: {}", id);
            return ResponseEntity.notFound().build();
        }
        logger.info("Returning event with ID: {}", id);
        return ResponseEntity.ok(event);
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@Valid @RequestBody Event event) {
        logger.info("Received request to create event: {}", event.getTitle());
        if (event.getOrganizer() == null || event.getOrganizer().getUserId() == null) {
            logger.warn("Bad request for create event: Organizer information missing in payload.");
            return ResponseEntity.badRequest().body(null);
        }
        Long organizerId = event.getOrganizer().getUserId();
        logger.debug("Attempting to create event with organizer ID: {}", organizerId);
        Event createdEvent = eventService.createEvent(event, organizerId);

        if (createdEvent == null) {
            logger.error("Failed to create event. Service returned null. Organizer ID: {}, Event Title: {}", organizerId, event.getTitle());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        logger.info("Successfully created event with ID: {}", createdEvent.getEventId());
        return ResponseEntity.created(URI.create("/api/events/" + createdEvent.getEventId())).body(createdEvent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @Valid @RequestBody Event eventDetails) {
        logger.info("Received request to update event with ID: {}", id);
        Event existingEvent = eventService.getEventById(id);
        if (existingEvent == null) {
            logger.warn("Cannot update event. Event not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }

        logger.debug("Attempting to update event ID: {} with details: {}", id, eventDetails.getTitle());
        Event updatedEvent = eventService.updateEvent(id, eventDetails);

        if (updatedEvent == null) {
            logger.error("Failed to update event with ID: {}. Service returned null.", id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        
        if (Objects.equals(updatedEvent.getEventId(), existingEvent.getEventId()) &&
            updatedEvent == existingEvent && 
            !Objects.equals(existingEvent.getOrganizer().getUserId(), null )) {
            logger.warn("Event update for ID {} might have been blocked (e.g., authorization). Service returned original object.", id);
        }
        logger.info("Successfully updated event with ID: {}", updatedEvent.getEventId());
        return ResponseEntity.ok(updatedEvent);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Event> patchEvent(@PathVariable Long id, @RequestBody Event partialEventDetails) {
        logger.info("Received request to patch event with ID: {}", id);
        Event existingEvent = eventService.getEventById(id);
        if (existingEvent == null) {
            logger.warn("Cannot patch event. Event not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }

        logger.debug("Attempting to patch event ID: {} with partial details.", id);
        Event patchedEvent = eventService.patchEvent(id, partialEventDetails); 

        if (patchedEvent == null) {
             logger.error("Failed to patch event with ID: {}. Service returned null.", id);
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        logger.info("Successfully patched event with ID: {}", patchedEvent.getEventId());
        return ResponseEntity.ok(patchedEvent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        logger.info("Received request to delete event with ID: {}", id);
        boolean deleted = eventService.deleteEvent(id); 

        if (!deleted) {
            Event eventExists = eventService.getEventById(id);
            if (eventExists == null) {
                logger.warn("Cannot delete event. Event not found with ID: {}", id);
                return ResponseEntity.notFound().build();
            }
            logger.warn("Failed to delete event with ID: {}. Deletion might be forbidden.", id);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); 
        }
        logger.info("Successfully deleted event with ID: {}", id);
        return ResponseEntity.ok().build();
    }
}