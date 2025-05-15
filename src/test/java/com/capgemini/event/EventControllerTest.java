package com.capgemini.event;

import com.capgemini.event.controllers.EventController;
import com.capgemini.event.entities.Event;
import com.capgemini.event.entities.User;
import com.capgemini.event.services.EventService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventControllerTest {

    @Mock
    private EventService eventService;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private EventController eventController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllEvents() {
        List<Event> events = Arrays.asList(new Event(), new Event());
        when(eventService.getAllEvents()).thenReturn(events);

        ResponseEntity<List<Event>> response = eventController.getAllEvents();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testGetEventById_Found() {
        Event event = new Event();
        event.setEventId(1L);
        when(eventService.getEventById(1L)).thenReturn(event);

        ResponseEntity<Event> response = eventController.getEventById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1L, response.getBody().getEventId());
    }

    @Test
    void testGetEventById_NotFound() {
        when(eventService.getEventById(100L)).thenReturn(null);

        ResponseEntity<Event> response = eventController.getEventById(100L);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testCreateEvent_Success() {
        Event event = new Event();
        User organizer = new User();
        organizer.setUserId(10L);
        event.setOrganizer(organizer);

        Event savedEvent = new Event();
        savedEvent.setEventId(1L);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(eventService.createEvent(event, 10L)).thenReturn(savedEvent);

        ResponseEntity<Event> response = eventController.createEvent(event, bindingResult);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(1L, response.getBody().getEventId());
    }

    @Test
    void testCreateEvent_InvalidData() {
        Event event = new Event();
        when(bindingResult.hasErrors()).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> {
            eventController.createEvent(event, bindingResult);
        });
    }

    @Test
    void testCreateEvent_MissingOrganizer() {
        Event event = new Event();
        when(bindingResult.hasErrors()).thenReturn(false);

        ResponseEntity<Event> response = eventController.createEvent(event, bindingResult);

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testUpdateEvent_FoundAndUpdated() {
        Event oldEvent = new Event();
        oldEvent.setEventId(1L);

        Event updatedEvent = new Event();
        updatedEvent.setEventId(1L);
        User organizer = new User();
        organizer.setUserId(10L);
        oldEvent.setOrganizer(organizer);
        updatedEvent.setOrganizer(organizer);

        when(eventService.getEventById(1L)).thenReturn(oldEvent);
        when(eventService.updateEvent(1L, updatedEvent)).thenReturn(updatedEvent);

        ResponseEntity<Event> response = eventController.updateEvent(1L, updatedEvent);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1L, response.getBody().getEventId());
    }

    @Test
    void testUpdateEvent_NotFound() {
        when(eventService.getEventById(999L)).thenReturn(null);

        ResponseEntity<Event> response = eventController.updateEvent(999L, new Event());

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testUpdateEvent_BadRequest() {
        Event eventDetails = new Event();
        Event oldEvent = new Event();
        oldEvent.setEventId(1L);
        when(eventService.getEventById(1L)).thenReturn(oldEvent);
        when(eventService.updateEvent(1L, eventDetails)).thenReturn(null);

        ResponseEntity<Event> response = eventController.updateEvent(1L, eventDetails);

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testPatchEvent_Success() {
        Event oldEvent = new Event();
        oldEvent.setEventId(1L);
        Event patchedEvent = new Event();
        patchedEvent.setEventId(1L);

        when(eventService.getEventById(1L)).thenReturn(oldEvent);
        when(eventService.patchEvent(1L, oldEvent)).thenReturn(patchedEvent);

        ResponseEntity<Event> response = eventController.patchEvent(1L, oldEvent);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1L, response.getBody().getEventId());
    }

    @Test
    void testPatchEvent_NotFound() {
        when(eventService.getEventById(5L)).thenReturn(null);

        ResponseEntity<Event> response = eventController.patchEvent(5L, new Event());

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testPatchEvent_BadRequest() {
        Event e = new Event();
        when(eventService.getEventById(1L)).thenReturn(e);
        when(eventService.patchEvent(1L, e)).thenReturn(null);

        ResponseEntity<Event> response = eventController.patchEvent(1L, e);

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testDeleteEvent_Success() {
        when(eventService.deleteEvent(1L)).thenReturn(true);

        ResponseEntity<Void> response = eventController.deleteEvent(1L);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testDeleteEvent_NotFound() {
        when(eventService.deleteEvent(1L)).thenReturn(false);
        when(eventService.getEventById(1L)).thenReturn(null);

        ResponseEntity<Void> response = eventController.deleteEvent(1L);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testDeleteEvent_Forbidden() {
        when(eventService.deleteEvent(1L)).thenReturn(false);
        when(eventService.getEventById(1L)).thenReturn(new Event());

        ResponseEntity<Void> response = eventController.deleteEvent(1L);

        assertEquals(403, response.getStatusCodeValue());
    }
}
