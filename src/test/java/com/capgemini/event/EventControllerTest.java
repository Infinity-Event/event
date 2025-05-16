package com.capgemini.event;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.capgemini.event.controllers.EventController;
import com.capgemini.event.entities.Event;
import com.capgemini.event.entities.User;
import com.capgemini.event.services.EventService;

class EventControllerTest {

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    public EventControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllEvents() {
        Event event1 = new Event();
        Event event2 = new Event();
        when(eventService.getAllEvents()).thenReturn(Arrays.asList(event1, event2));

        ResponseEntity<List<Event>> response = eventController.getAllEvents();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testGetEventByIdFound() {
        Event event = new Event();
        when(eventService.getEventById(1L)).thenReturn(event);

        ResponseEntity<Event> response = eventController.getEventById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testUpdateEventSuccess() {
        Event existing = new Event();
        User user = new User();
        user.setUserId(1L);
        existing.setOrganizer(user);
        existing.setEventId(1L);

        Event updated = new Event();
        updated.setOrganizer(user);
        updated.setEventId(1L);

        when(eventService.getEventById(1L)).thenReturn(existing);
        when(eventService.updateEvent(eq(1L), any())).thenReturn(updated);

        ResponseEntity<Event> response = eventController.updateEvent(1L, updated);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testDeleteEventSuccess() {
        when(eventService.deleteEvent(1L)).thenReturn(true);

        ResponseEntity<Void> response = eventController.deleteEvent(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
