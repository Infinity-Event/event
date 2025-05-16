package com.capgemini.event.controllers;

import com.capgemini.event.entities.Event;
import com.capgemini.event.services.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/events")
@Slf4j
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        log.info("Received request to get all events");
        List<Event> events = eventService.getAllEvents();
        log.info("Returning {} events", events.size());
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        log.info("Received request to get event by ID: {}", id);
        Event event = eventService.getEventById(id);
        if (event == null) {
            log.warn("Event not found for ID: {}", id);
            return ResponseEntity.notFound().build();
        }
        log.info("Returning event with ID: {}", id);
        return ResponseEntity.ok(event);
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@Valid @RequestBody Event event) {
        log.info("Received request to create event: {}", event.getTitle());
        if (event.getOrganizer() == null || event.getOrganizer().getUserId() == null) {
            log.warn("Bad request for create event: Organizer information missing in payload.");
            return ResponseEntity.badRequest().body(null);
        }
        Long organizerId = event.getOrganizer().getUserId();
        log.debug("Attempting to create event with organizer ID: {}", organizerId);
        Event createdEvent = eventService.createEvent(event, organizerId);

        if (createdEvent == null) {
            log.error("Failed to create event. Service returned null. Organizer ID: {}, Event Title: {}", organizerId, event.getTitle());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        log.info("Successfully created event with ID: {}", createdEvent.getEventId());
        return ResponseEntity.created(URI.create("/api/events/" + createdEvent.getEventId())).body(createdEvent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @Valid @RequestBody Event eventDetails) {
        log.info("Received request to update event with ID: {}", id);
        Event existingEvent = eventService.getEventById(id);
        if (existingEvent == null) {
            log.warn("Cannot update event. Event not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }

        log.debug("Attempting to update event ID: {} with details: {}", id, eventDetails.getTitle());
        Event updatedEvent = eventService.updateEvent(id, eventDetails);

        if (updatedEvent == null) {
            log.error("Failed to update event with ID: {}. Service returned null.", id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        
        if (Objects.equals(updatedEvent.getEventId(), existingEvent.getEventId()) &&
            updatedEvent == existingEvent && 
            !Objects.equals(existingEvent.getOrganizer().getUserId(), null )) {
            log.warn("Event update for ID {} might have been blocked or no changes made. Service returned original object instance.", id);
        }
        log.info("Successfully updated event with ID: {}", updatedEvent.getEventId());
        return ResponseEntity.ok(updatedEvent);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Event> patchEvent(@PathVariable Long id, @RequestBody Event partialEventDetails) {
        log.info("Received request to patch event with ID: {}", id);
        Event existingEvent = eventService.getEventById(id);
        if (existingEvent == null) {
            log.warn("Cannot patch event. Event not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }

        log.debug("Attempting to patch event ID: {} with partial details.", id);
        Event patchedEvent = eventService.patchEvent(id, partialEventDetails); 

        if (patchedEvent == null) {
             log.error("Failed to patch event with ID: {}. Service returned null.", id);
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        log.info("Successfully patched event with ID: {}", patchedEvent.getEventId());
        return ResponseEntity.ok(patchedEvent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        log.info("Received request to delete event with ID: {}", id);
        boolean deleted = eventService.deleteEvent(id); 

        if (!deleted) {
            Event eventExists = eventService.getEventById(id);
            if (eventExists == null) {
                log.warn("Cannot delete event. Event not found with ID: {}", id);
                return ResponseEntity.notFound().build();
            }
            log.warn("Failed to delete event with ID: {}. Deletion might be forbidden.", id);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); 
        }
        log.info("Event with ID {} successfully deleted", id);
        return ResponseEntity.noContent().build(); 
    }
}