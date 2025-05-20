package com.capgemini.event;

import com.capgemini.event.entities.Event;
import com.capgemini.event.entities.User;
import com.capgemini.event.entities.UserType;
import com.capgemini.event.repositories.EventRepo;
import com.capgemini.event.repositories.UserRepo;
import com.capgemini.event.services.EventServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EventServiceImplTest {

	@Mock
	private EventRepo eventRepo;

	@Mock
	private UserRepo userRepo;

	@InjectMocks
	private EventServiceImpl eventService;

	private Event sampleEvent;
	private User sampleOrganizer;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		sampleOrganizer = new User();
		sampleOrganizer.setUserId(1L);
		sampleOrganizer.setName("Test Organizer");
		sampleOrganizer.setEmail("organizer@example.com");
		sampleOrganizer.setType(UserType.ORGANIZER);

		sampleEvent = new Event("Sample Event", "Event Description", LocalDate.now(), LocalTime.now(), "Event Location",
				100, null);
		sampleEvent.setOrganizer(sampleOrganizer);
		sampleEvent.setEventId(1L);
	}

	@Test
	void createEvent_success() {
		when(userRepo.findById(1L)).thenReturn(Optional.of(sampleOrganizer));
		when(eventRepo.save(any(Event.class))).thenReturn(sampleEvent);

		Event eventToCreate = new Event("New Event", "New Desc", LocalDate.now().plusDays(5),
				LocalTime.now().plusHours(2), "New Location", 50, null);
		eventToCreate.setOrganizer(sampleOrganizer);
		Event createdEvent = eventService.createEvent(eventToCreate, 1L);

		assertNotNull(createdEvent);
		assertEquals(sampleEvent.getEventId(), createdEvent.getEventId());
		verify(userRepo, times(1)).findById(1L);
		verify(eventRepo, times(1)).save(any(Event.class));
	}

	@Test
	void getEventById_found() {
		when(eventRepo.findById(1L)).thenReturn(Optional.of(sampleEvent));

		Event foundEvent = eventService.getEventById(1L);

		assertNotNull(foundEvent);
		assertEquals(sampleEvent.getEventId(), foundEvent.getEventId());
		verify(eventRepo, times(1)).findById(1L);
	}

	@Test
	void getAllEvents() {
		List<Event> events = Arrays.asList(sampleEvent, new Event("Event 2", "Desc 2", LocalDate.now(), LocalTime.now(), "Loc 2", 20, null));
		when(eventRepo.findAll()).thenReturn(events);

		List<Event> foundEvents = eventService.getAllEvents();

		assertNotNull(foundEvents);
		assertEquals(2, foundEvents.size());
		verify(eventRepo, times(1)).findAll();
	}

	@Test
	void getEventsByOrganizer_success() {
		when(userRepo.findById(1L)).thenReturn(Optional.of(sampleOrganizer));
		when(eventRepo.findByOrganizer(sampleOrganizer)).thenReturn(Arrays.asList(sampleEvent));

		List<Event> result = eventService.getEventsByOrganizer(1L);

		assertNotNull(result);
		assertFalse(result.isEmpty());
		assertEquals(sampleEvent.getEventId(), result.get(0).getEventId());
		verify(userRepo, times(1)).findById(1L);
		verify(eventRepo, times(1)).findByOrganizer(sampleOrganizer);
	}

	@Test
	void updateEvent_success() {
		Event eventDetailsToUpdate = new Event("Updated Title", "Updated Desc", LocalDate.now().plusDays(1),
				LocalTime.now().plusHours(1), "New Location", 150, null);
		eventDetailsToUpdate.setOrganizer(sampleOrganizer);
		Event existingEvent = new Event("Original Title", "Original Desc", LocalDate.now(), LocalTime.now(),
				"Original Location", 100, null);
		existingEvent.setOrganizer(sampleOrganizer);
		existingEvent.setEventId(1L);

		when(eventRepo.findById(1L)).thenReturn(Optional.of(existingEvent));
		when(eventRepo.save(any(Event.class))).thenAnswer(invocation -> {
			Event savedEvent = invocation.getArgument(0);
			savedEvent.setEventId(1L); // Ensure ID is preserved/set
			return savedEvent;
		});

		Event updatedEvent = eventService.updateEvent(1L, eventDetailsToUpdate);

		assertNotNull(updatedEvent);
		assertEquals("Updated Title", updatedEvent.getTitle());
		assertEquals(1L, updatedEvent.getEventId());
		verify(eventRepo, times(1)).findById(1L);
		verify(eventRepo, times(1)).save(any(Event.class));
	}

	@Test
	void patchEvent_success() {
		Event partialDetails = new Event();
		partialDetails.setTitle("Patched Title");

		Event existingEvent = new Event("Original Title", "Original Desc", LocalDate.now(), LocalTime.now(),
				"Original Location", 100, null);
		existingEvent.setOrganizer(sampleOrganizer);
		existingEvent.setEventId(1L);

		when(eventRepo.findById(1L)).thenReturn(Optional.of(existingEvent));
		when(eventRepo.save(any(Event.class))).thenAnswer(invocation -> {
			Event savedEvent = invocation.getArgument(0);
			savedEvent.setEventId(1L);
			return savedEvent;
		});

		Event patchedEvent = eventService.patchEvent(1L, partialDetails);

		assertNotNull(patchedEvent);
		assertEquals("Patched Title", patchedEvent.getTitle());
		assertEquals(existingEvent.getDescription(), patchedEvent.getDescription());
		assertEquals(1L, patchedEvent.getEventId());
		verify(eventRepo, times(1)).findById(1L);
		verify(eventRepo, times(1)).save(any(Event.class));
	}

	@Test
	void deleteEvent_success() {
		when(eventRepo.existsById(1L)).thenReturn(true);
		doNothing().when(eventRepo).deleteById(1L);

		boolean deleted = eventService.deleteEvent(1L);

		assertTrue(deleted);
		verify(eventRepo, times(1)).existsById(1L);
		verify(eventRepo, times(1)).deleteById(1L);
	}
}
