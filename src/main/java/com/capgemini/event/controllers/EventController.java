package com.capgemini.event.controllers;

import com.capgemini.event.entities.Event;
import com.capgemini.event.services.EventService;
import jakarta.validation.Valid;
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

    @Autowired
    private EventService eventService;

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        Event event = eventService.getEventById(id);
        if (event == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(event);
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@Valid @RequestBody Event event) {
        if (event.getOrganizer() == null || event.getOrganizer().getUserId() == null) {
            return ResponseEntity.badRequest().body(null);
        }
        Long organizerId = event.getOrganizer().getUserId();
        Event createdEvent = eventService.createEvent(event, organizerId);

        if (createdEvent == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.created(URI.create("/api/events/" + createdEvent.getEventId())).body(createdEvent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @Valid @RequestBody Event eventDetails) {
        Event existingEvent = eventService.getEventById(id);
        if (existingEvent == null) {
            return ResponseEntity.notFound().build();
        }

        Event updatedEvent = eventService.updateEvent(id, eventDetails);

        if (updatedEvent == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        
        if (Objects.equals(updatedEvent.getEventId(), existingEvent.getEventId()) &&
            updatedEvent == existingEvent &&
            !Objects.equals(existingEvent.getOrganizer().getUserId(), null )) { 
        }
        return ResponseEntity.ok(updatedEvent);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Event> patchEvent(@PathVariable Long id, @RequestBody Event partialEventDetails) {
        Event existingEvent = eventService.getEventById(id);
        if (existingEvent == null) {
            return ResponseEntity.notFound().build();
        }

        Event patchedEvent = eventService.patchEvent(id, partialEventDetails); 

        if (patchedEvent == null) {
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.ok(patchedEvent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        boolean deleted = eventService.deleteEvent(id); 

        if (!deleted) {
            Event eventExists = eventService.getEventById(id);
            if (eventExists == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); 
        }
        return ResponseEntity.ok().build();
    }
}