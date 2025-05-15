package com.capgemini.event;

import com.capgemini.event.entities.Event;
import com.capgemini.event.entities.Registration;
import com.capgemini.event.entities.User;
import com.capgemini.event.repositories.EventRepo;
import com.capgemini.event.repositories.RegistrationRepo;
import com.capgemini.event.repositories.UserRepo;
import com.capgemini.event.services.RegistrationServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegistrationServiceImplTest {

    @Mock
    private RegistrationRepo registrationRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private EventRepo eventRepo;

    @InjectMocks
    private RegistrationServiceImpl registrationService;

    private User user;
    private Event event;
    private Registration registration;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User(1L, "John", "john@example.com", "pass", "1234567890", null);
        event = new Event();
        event.setEventId(1L);
        registration = new Registration(null, null, null, user, event);
        registration.setRegDate(LocalDate.now());
    }


    @Test
    void testGetRegistrationById_Found() {
        when(registrationRepo.findById(1L)).thenReturn(Optional.of(registration));

        Registration result = registrationService.getRegistrationById(1L);

        assertNotNull(result);
        assertEquals(user, result.getUser());
    }

    @Test
    void testGetRegistrationById_NotFound() {
        when(registrationRepo.findById(99L)).thenReturn(Optional.empty());

        Registration result = registrationService.getRegistrationById(99L);
        assertNull(result);
    }

    @Test
    void testGetAllRegistrations() {
        when(registrationRepo.findAll()).thenReturn(Arrays.asList(registration));

        List<Registration> result = registrationService.getAllRegistrations();

        assertEquals(1, result.size());
        verify(registrationRepo).findAll();
    }

    @Test
    void testUpdateRegistration_Success() {
        when(registrationRepo.findById(1L)).thenReturn(Optional.of(registration));
        when(registrationRepo.save(any(Registration.class))).thenReturn(registration);

        Registration updateDetails = new Registration();
        updateDetails.setRegDate(LocalDate.of(2025, 1, 1));

        Registration result = registrationService.updateRegistration(1L, updateDetails);

        assertNotNull(result);
        assertEquals(LocalDate.of(2025, 1, 1), result.getRegDate());
    }

    @Test
    void testUpdateRegistration_NotFound() {
        when(registrationRepo.findById(99L)).thenReturn(Optional.empty());

        Registration result = registrationService.updateRegistration(99L, new Registration());

        assertNull(result);
    }

    @Test
    void testDeleteRegistration_Success() {
        when(registrationRepo.existsById(1L)).thenReturn(true);
        doNothing().when(registrationRepo).deleteById(1L);

        boolean deleted = registrationService.deleteRegistration(1L);

        assertTrue(deleted);
        verify(registrationRepo).deleteById(1L);
    }

    @Test
    void testDeleteRegistration_NotFound() {
        when(registrationRepo.existsById(99L)).thenReturn(false);

        boolean deleted = registrationService.deleteRegistration(99L);
        assertFalse(deleted);
    }

    @Test
    void testGetRegistrationsByUserId_Found() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(registrationRepo.findByUser(user)).thenReturn(List.of(registration));

        List<Registration> result = registrationService.getRegistrationsByUserId(1L);

        assertEquals(1, result.size());
    }

    @Test
    void testGetRegistrationsByUserId_NotFound() {
        when(userRepo.findById(99L)).thenReturn(Optional.empty());

        List<Registration> result = registrationService.getRegistrationsByUserId(99L);

        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void testGetRegistrationsByEventId_Found() {
        when(eventRepo.findById(1L)).thenReturn(Optional.of(event));
        when(registrationRepo.findByEvent(event)).thenReturn(List.of(registration));

        List<Registration> result = registrationService.getRegistrationsByEventId(1L);

        assertEquals(1, result.size());
    }

    @Test
    void testGetRegistrationsByEventId_NotFound() {
        when(eventRepo.findById(99L)).thenReturn(Optional.empty());

        List<Registration> result = registrationService.getRegistrationsByEventId(99L);

        assertEquals(Collections.emptyList(), result);
    }
}
