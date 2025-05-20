package com.capgemini.event.services;

import com.capgemini.event.entities.Event;
import com.capgemini.event.entities.User;
import com.capgemini.event.entities.UserType;
import com.capgemini.event.exceptions.EventNotFoundException;
import com.capgemini.event.exceptions.UserNotFoundException;
import com.capgemini.event.repositories.EventRepo;
import com.capgemini.event.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepo eventRepo;
    private final UserRepo userRepo;

    @Override
    @Transactional
    public Event createEvent(Event event, Long organizerId) {
        log.info("Attempting to create event titled '{}' for organizer ID: {}", event.getTitle(), organizerId);

        Optional<User> organizerOptional = userRepo.findById(organizerId);
        if (organizerOptional.isEmpty()) {
            log.warn("Failed to create event: Organizer not found with ID: {}", organizerId);
            throw new UserNotFoundException("Organizer not found with Id: " + organizerId);
        }
        User organizer = organizerOptional.get();
        if (organizer.getType() != UserType.ORGANIZER) {
            log.warn("Failed to create event: User with ID {} is not an ORGANIZER. Actual type: {}", organizerId,
                    organizer.getType());
            throw new UserNotFoundException("Organizer not found");
        }
        event.setOrganizer(organizer);
        Event savedEvent = eventRepo.save(event);
        log.info("Successfully created event with ID: {} and title: '{}'", savedEvent.getEventId(),
                savedEvent.getTitle());
        return savedEvent;
    }

    @Override
    public Event getEventById(Long eventId) {
        log.debug("Fetching event by ID: {}", eventId);
        Optional<Event> eventOptional = eventRepo.findById(eventId);
        if (eventOptional.isEmpty()) {
            log.warn("Event not found with ID: {}", eventId);
            throw new EventNotFoundException("Event with id " + eventId + " not found");
        }
        Event event = eventOptional.get();
        log.debug("Found event: {}", event.getTitle());
        return event;
    }

    @Override
    public List<Event> getAllEvents() {
        log.debug("Fetching all events");
        List<Event> events = eventRepo.findAll();
        log.debug("Found {} events", events.size());
        return events;
    }

    @Override
    @Transactional
    public Event updateEvent(Long eventId, Event eventDetails) {
        log.info("Attempting to update event with ID: {}", eventId);
        Optional<Event> eventOptional = eventRepo.findById(eventId);
        if (eventOptional.isEmpty()) {
            log.warn("Failed to update event: Event not found with ID: {}", eventId);
        }
        Event existingEvent = eventOptional.get();
        log.debug("Updating event '{}'. Original details: {}", existingEvent.getTitle(), existingEvent);
        log.debug("New details for update: {}", eventDetails);

        existingEvent.setTitle(eventDetails.getTitle());
        existingEvent.setDescription(eventDetails.getDescription());
        existingEvent.setDate(eventDetails.getDate());
        existingEvent.setTime(eventDetails.getTime());
        existingEvent.setLocation(eventDetails.getLocation());
        existingEvent.setCapacity(eventDetails.getCapacity());
        existingEvent.setCategory(eventDetails.getCategory());

        Event updatedEvent = eventRepo.save(existingEvent);
        log.info("Successfully updated event with ID: {}. New title: '{}'", updatedEvent.getEventId(),
                updatedEvent.getTitle());
        return updatedEvent;
    }

    @Override
    @Transactional
    public Event patchEvent(Long eventId, Event partialEventDetails) {
        log.info("Attempting to patch event with ID: {}", eventId);
        Optional<Event> eventOptional = eventRepo.findById(eventId);
        if (eventOptional.isEmpty()) {
            log.warn("Failed to patch event: Event not found with ID: {}", eventId);
            throw new EventNotFoundException("Event with id " + eventId + " not found");
        }
        Event existingEvent = eventOptional.get();
        log.debug("Patching event '{}'. Original details: {}", existingEvent.getTitle(), existingEvent);
        log.debug("Partial details for patch: {}", partialEventDetails);

        if (partialEventDetails.getTitle() != null)
            existingEvent.setTitle(partialEventDetails.getTitle());
        else if (partialEventDetails.getDescription() != null)
            existingEvent.setDescription(partialEventDetails.getDescription());
        else if (partialEventDetails.getDate() != null)
            existingEvent.setDate(partialEventDetails.getDate());
        else if (partialEventDetails.getTime() != null)
            existingEvent.setTime(partialEventDetails.getTime());
        else if (partialEventDetails.getLocation() != null)
            existingEvent.setLocation(partialEventDetails.getLocation());
        else if (partialEventDetails.getCapacity() != null)
            existingEvent.setCapacity(partialEventDetails.getCapacity());
        else if (partialEventDetails.getCategory() != null)
            existingEvent.setCategory(partialEventDetails.getCategory());
        Event patchedEvent = eventRepo.save(existingEvent);
        log.info("Successfully patched event with ID: {}. Current title: '{}'", patchedEvent.getEventId(),
                patchedEvent.getTitle());
        return patchedEvent;
    }

    @Override
    @Transactional
    public boolean deleteEvent(Long eventId) {
        log.info("Attempting to delete event with ID: {}", eventId);
        if (!eventRepo.existsById(eventId)) {
            log.warn("Failed to delete event: Event not found with ID: {}", eventId);
            return false;
        }
        eventRepo.deleteById(eventId);
        log.info("Successfully deleted event with ID: {}", eventId);
        return true;
    }

    @Override
    public List<Event> getEventsByOrganizer(Long organizerId) {
        log.debug("Fetching events for organizer ID: {}", organizerId);
        Optional<User> organizerOptional = userRepo.findById(organizerId);
        if (organizerOptional.isEmpty()) {
            log.warn("Cannot fetch events by organizer: Organizer not found with ID: {}", organizerId);
            return Collections.emptyList();
        }
        List<Event> events = eventRepo.findByOrganizer(organizerOptional.get());
        log.debug("Found {} events for organizer ID: {}", events.size(), organizerId);
        return events;
    }

    @Override
    public List<Event> getEventsByOrganizerEmail(String organizerEmail) {
        log.debug("Fetching events for organizer email: {}", organizerEmail);
        Optional<User> organizerOptional = userRepo.findByEmail(organizerEmail);
        if (organizerOptional.isEmpty()) {
            log.warn("Cannot fetch events by organizer: Organizer not found with email: {}", organizerEmail);
            return Collections.emptyList();
        }
        List<Event> events = eventRepo.findByOrganizer(organizerOptional.get());
        log.debug("Found {} events for organizer email: {}", events.size(), organizerEmail);
        return events;
    }
}