package com.capgemini.event;

import com.capgemini.event.controllers.RegistrationRestController;
import com.capgemini.event.entities.Category;
import com.capgemini.event.entities.Event;
import com.capgemini.event.entities.Registration;
import com.capgemini.event.entities.User;
import com.capgemini.event.services.RegistrationService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RegistrationRestController.class)
class RegistrationRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegistrationService registrationService;

    private Registration registration;
    private User user;
    private Event event;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1L);

        event = new Event();
        event.setEventId(1L);
        event.setCapacity(20);
        event.setCategory(Category.WEBINAR);
        event.setDate(LocalDate.of(2020,5,12));
        event.setDescription("Event 1");
        event.setLocation("Mumbai");

        registration = new Registration();
        registration.setRegId(1L);
        registration.setUser(user);
        registration.setEvent(event);
        registration.setRegDate(LocalDate.now());
    }

    @Test
    void testGetAllRegistrations() throws Exception {
        Mockito.when(registrationService.getAllRegistrations()).thenReturn(Arrays.asList(registration));

        mockMvc.perform(get("/api/registrations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void testGetRegistrationById_Found() throws Exception {
        Mockito.when(registrationService.getRegistrationById(1L)).thenReturn(registration);

        mockMvc.perform(get("/api/registrations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.regId").value(1));
    }

//    @Test
//    void testGetRegistrationById_NotFound() throws Exception {
//        Mockito.when(registrationService.getRegistrationById(99L)).thenReturn(null);
//
//        mockMvc.perform(get("/api/registrations/99"))
//                .andExpect(status().isNotFound());
//    }

//    @Test
//    void testCreateRegistration_Success() throws Exception {
//        Mockito.when(registrationService.createRegistration(any(Registration.class))).thenReturn(registration);
//
//        mockMvc.perform(post("/api/registrations")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(new ObjectMapper().writeValueAsString(registration)))
//                .andExpect(status().isCreated())
//                .andExpect(header().string("Location", "/api/registrations/1"))
//                .andExpect(jsonPath("$.regId").value(1));
//    }

//    @Test
//    void testCreateRegistration_BadRequest() throws Exception {
//        Mockito.when(registrationService.createRegistration(any(Registration.class))).thenReturn(null);
//
//        mockMvc.perform(post("/api/registrations")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(new ObjectMapper().writeValueAsString(registration)))
//                .andExpect(status().isBadRequest());
//    }

//    @Test
//    void testUpdateRegistration_Success() throws Exception {
//        Mockito.when(registrationService.getRegistrationById(1L)).thenReturn(registration);
//        Mockito.when(registrationService.updateRegistration(eq(1L), any(Registration.class))).thenReturn(registration);
//
//        mockMvc.perform(put("/api/registrations/1")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(new ObjectMapper().writeValueAsString(registration)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.regId").value(1));
//    }

//    @Test
//    void testUpdateRegistration_NotFound() throws Exception {
//        Mockito.when(registrationService.getRegistrationById(99L)).thenReturn(null);
//
//        mockMvc.perform(put("/api/registrations/99")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(new ObjectMapper().writeValueAsString(registration)))
//                .andExpect(status().isNotFound());
//    }

    @Test
    void testDeleteRegistration_Success() throws Exception {
        Mockito.when(registrationService.deleteRegistration(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/registrations/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteRegistration_NotFound() throws Exception {
        Mockito.when(registrationService.deleteRegistration(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/registrations/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetRegistrationsByUserId() throws Exception {
        Mockito.when(registrationService.getRegistrationsByUserId(1L)).thenReturn(Collections.singletonList(registration));

        mockMvc.perform(get("/api/registrations/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void testGetRegistrationsByEventId() throws Exception {
        Mockito.when(registrationService.getRegistrationsByEventId(1L)).thenReturn(Collections.singletonList(registration));

        mockMvc.perform(get("/api/registrations/event/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }
}
