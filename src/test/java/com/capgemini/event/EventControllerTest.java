package com.capgemini.event;

import com.capgemini.event.controllers.EventController;
import com.capgemini.event.entities.Event;
import com.capgemini.event.entities.User;
import com.capgemini.event.entities.UserType;
import com.capgemini.event.services.EventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class EventControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    private ObjectMapper objectMapper;
    private Event sampleEvent;
    private Event sampleEvent2;
    private User sampleOrganizer;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(eventController).build();

        sampleOrganizer = new User();
        sampleOrganizer.setUserId(1L);
        sampleOrganizer.setName("Test Organizer");
        sampleOrganizer.setEmail("organizer@example.com");
        sampleOrganizer.setType(UserType.ORGANIZER);

        sampleEvent = new Event("Sample Event", "Event Description", LocalDate.now(), LocalTime.now(), "Event Location", 100, null, sampleOrganizer);
        sampleEvent.setEventId(1L);

        sampleEvent2 = new Event("Another Event", "Another Description", LocalDate.now().plusDays(1), LocalTime.now().plusHours(2), "Another Location", 50, null, sampleOrganizer);
        sampleEvent2.setEventId(2L);
    }

    private String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void createEvent_success() throws Exception {
        Event eventToCreate = new Event("New Event", "New Desc", LocalDate.now().plusDays(5), LocalTime.now().plusHours(2), "New Location", 50, null, sampleOrganizer);
        Event createdEvent = new Event("New Event", "New Desc", LocalDate.now().plusDays(5), LocalTime.now().plusHours(2), "New Location", 50, null, sampleOrganizer);
        createdEvent.setEventId(3L);

        when(eventService.createEvent(any(Event.class), anyLong())).thenReturn(createdEvent);

        mockMvc.perform(post("/events")
                        .param("organizerId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(eventToCreate)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.eventId").value(3L))
                .andExpect(jsonPath("$.title").value("New Event"));
    }

    @Test
    void getEventById_found() throws Exception {
        when(eventService.getEventById(1L)).thenReturn(sampleEvent);

        mockMvc.perform(get("/events/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventId").value(1L))
                .andExpect(jsonPath("$.title").value("Sample Event"));
    }

    @Test
    void getAllEvents_success() throws Exception {
        List<Event> events = Arrays.asList(sampleEvent, sampleEvent2);
        when(eventService.getAllEvents()).thenReturn(events);

        mockMvc.perform(get("/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].title").value("Sample Event"))
                .andExpect(jsonPath("$[1].title").value("Another Event"));
    }

    @Test
    void getEventsByOrganizer_success() throws Exception {
        List<Event> organizerEvents = Arrays.asList(sampleEvent);
        when(eventService.getEventsByOrganizer(1L)).thenReturn(organizerEvents);

        mockMvc.perform(get("/events/organizer/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].eventId").value(sampleEvent.getEventId()));
    }

    @Test
    void updateEvent_success() throws Exception {
        Event eventDetailsToUpdate = new Event("Updated Title", "Updated Desc", LocalDate.now().plusDays(1), LocalTime.now().plusHours(1), "New Location", 150, null, sampleOrganizer);
        Event updatedEvent = new Event("Updated Title", "Updated Desc", LocalDate.now().plusDays(1), LocalTime.now().plusHours(1), "New Location", 150, null, sampleOrganizer);
        updatedEvent.setEventId(1L);

        when(eventService.updateEvent(anyLong(), any(Event.class))).thenReturn(updatedEvent);

        mockMvc.perform(put("/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(eventDetailsToUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.eventId").value(1L));
    }

    @Test
    void patchEvent_success() throws Exception {
        Event partialDetails = new Event();
        partialDetails.setTitle("Patched Title");        Event patchedEvent = new Event("Patched Title", sampleEvent.getDescription(), sampleEvent.getDate(), sampleEvent.getTime(), sampleEvent.getLocation(), sampleEvent.getCapacity(), sampleEvent.getCategory(), sampleEvent.getOrganizer());
        patchedEvent.setEventId(1L);

        when(eventService.patchEvent(anyLong(), any(Event.class))).thenReturn(patchedEvent);

        mockMvc.perform(patch("/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(partialDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Patched Title"))
                .andExpect(jsonPath("$.description").value(sampleEvent.getDescription()))
                .andExpect(jsonPath("$.eventId").value(1L));
    }

    @Test
    void deleteEvent_success() throws Exception {
        when(eventService.deleteEvent(1L)).thenReturn(true);

        mockMvc.perform(delete("/events/1"))
                .andExpect(status().isNoContent());
    }
}
