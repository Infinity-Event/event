package com.capgemini.event;

import com.capgemini.event.controllers.RegistrationRestController;
import com.capgemini.event.entities.Category;
import com.capgemini.event.entities.Event;
import com.capgemini.event.entities.Registration;
import com.capgemini.event.entities.User;
import com.capgemini.event.services.RegistrationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RegistrationRestControllerTest {

    @Mock
    private RegistrationService registrationService;

    @InjectMocks
    private RegistrationRestController registrationRestController;

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
        event.setDate(LocalDate.of(2020, 5, 12));
        event.setDescription("Event 1");
        event.setLocation("Mumbai");

        registration = new Registration();
        registration.setRegId(1L);
        registration.setUser(user);
        registration.setEvent(event);
        registration.setRegDate(LocalDate.now());
    }

    @Test
    void testGetAllRegistrations() {
        Mockito.when(registrationService.getAllRegistrations()).thenReturn(Arrays.asList(registration));
        List<Registration> expectedRegistrations = Arrays.asList(registration);
        ResponseEntity<List<Registration>> response = registrationRestController.getAllRegistrations();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedRegistrations, response.getBody());
        verify(registrationService).getAllRegistrations();
    }

    @Test
    void testGetRegistrationById_Found() {
        Mockito.when(registrationService.getRegistrationById(1L)).thenReturn(registration);

        ResponseEntity<Registration> response = registrationRestController.getRegistrationById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(registration, response.getBody());
        verify(registrationService).getRegistrationById(1L);
    }

    @Test
    void testDeleteRegistration_Success() {
        Mockito.when(registrationService.deleteRegistration(1L)).thenReturn(true);
        ResponseEntity<Void> response = registrationRestController.deleteRegistration(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(null, response.getBody());
        verify(registrationService, times(1)).deleteRegistration(1L);
    }

    @Test
    void testDeleteRegistration_NotFound() {
        Mockito.when(registrationService.deleteRegistration(99L)).thenReturn(false);
        ResponseEntity<Void> response = registrationRestController.deleteRegistration(99L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());
        verify(registrationService, times(1)).deleteRegistration(99L);
    }

    @Test
    void testGetRegistrationsByUserId() {
        Mockito.when(registrationService.getRegistrationsByUserId(1L))
                .thenReturn(Collections.singletonList(registration));
        List<Registration> expectedRegistrations = Arrays.asList(registration);
        ResponseEntity<List<Registration>> response = registrationRestController.getRegistrationsByUserId(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedRegistrations, response.getBody());
        verify(registrationService).getRegistrationsByUserId(1L);
    }

    @Test
    void testGetRegistrationsByEventId() {
        Mockito.when(registrationService.getRegistrationsByEventId(1L))
                .thenReturn(Collections.singletonList(registration));
        List<Registration> expectedRegistrations = Arrays.asList(registration);
        ResponseEntity<List<Registration>> response = registrationRestController.getRegistrationsByEventId(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedRegistrations, response.getBody());
        verify(registrationService).getRegistrationsByEventId(1L);
    }
}
