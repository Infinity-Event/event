package com.capgemini.event.services;

import com.capgemini.event.entities.Event;
import com.capgemini.event.entities.User;
import com.capgemini.event.entities.UserType;
import com.capgemini.event.exceptions.EventNotFoundException;
import com.capgemini.event.repositories.EventRepo;
import com.capgemini.event.repositories.UserRepo;
import com.capgemini.event.services.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {

    private static final Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);
    private static final String EVENT_NOT_FOUND_MESSAGE = "Event not found with ID: ";

    private final EventRepo eventRepo;
    private final UserRepo userRepo;

    @Autowired
    public EventServiceImpl(EventRepo eventRepo, UserRepo userRepo) {
        this.eventRepo = eventRepo;
        this.userRepo = userRepo;
    }

    @Override
    @Transactional
    public Event createEvent(Event event, Long organizerId) {
        logger.info("Attempting to create event titled '{}' for organizer ID: {}", event.getTitle(), organizerId);

        Optional<User> organizerOptional = userRepo.findById(organizerId);
        if (organizerOptional.isEmpty()) {
            logger.warn("Failed to create event: Organizer not found with ID: {}", organizerId);
            return null;
        }
        User organizer = organizerOptional.get();
        if (organizer.getType() != UserType.ORGANIZER) {
            logger.warn("Failed to create event: User with ID {} is not an ORGANIZER. Actual type: {}", organizerId, organizer.getType());
            return null;
        }
        event.setOrganizer(organizer);
        event.setEventId(null);
        Event savedEvent = eventRepo.save(event);
        logger.info("Successfully created event with ID: {} and title: '{}'", savedEvent.getEventId(), savedEvent.getTitle());
        return savedEvent;
    }    @Override
    public Event getEventById(Long eventId) {
        logger.debug("Fetching event by ID: {}", eventId);        return eventRepo.findById(eventId)
            .orElseThrow(() -> {
                logger.warn("Event not found with ID: {}", eventId);
                return new EventNotFoundException(EVENT_NOT_FOUND_MESSAGE + eventId);
            });
    }

    @Override
    public List<Event> getAllEvents() {
        logger.debug("Fetching all events");
        List<Event> events = eventRepo.findAll();
        logger.debug("Found {} events", events.size());
        return events;
    }

    @Override
    public List<Event> getEventsByOrganizer(Long organizerId) {
        logger.debug("Fetching events for organizer ID: {}", organizerId);
        Optional<User> organizerOptional = userRepo.findById(organizerId);
        if (organizerOptional.isEmpty()) {
            logger.warn("Cannot fetch events by organizer: Organizer not found with ID: {}", organizerId);
            return Collections.emptyList();
        }
        List<Event> events = eventRepo.findByOrganizer(organizerOptional.get());
        logger.debug("Found {} events for organizer ID: {}", events.size(), organizerId);
        return events;
    }    @Override
    @Transactional
    public Event updateEvent(Long eventId, Event eventDetails) {
        logger.info("Attempting to update event with ID: {}", eventId);        Event existingEvent = eventRepo.findById(eventId)
            .orElseThrow(() -> {
                logger.warn("Failed to update event: Event not found with ID: {}", eventId);
                return new EventNotFoundException(EVENT_NOT_FOUND_MESSAGE + eventId);
            });
        
        logger.debug("Updating event '{}'. Original details: {}", existingEvent.getTitle(), existingEvent);
        logger.debug("New details for update: {}", eventDetails);

        existingEvent.setTitle(eventDetails.getTitle());
        existingEvent.setDescription(eventDetails.getDescription());
        existingEvent.setDate(eventDetails.getDate());
        existingEvent.setTime(eventDetails.getTime());
        existingEvent.setLocation(eventDetails.getLocation());
        existingEvent.setCapacity(eventDetails.getCapacity());
        existingEvent.setCategory(eventDetails.getCategory());
        
        Event updatedEvent = eventRepo.save(existingEvent);
        logger.info("Successfully updated event with ID: {}. New title: '{}'", updatedEvent.getEventId(), updatedEvent.getTitle());
        return updatedEvent;
    }    @Override
    @Transactional
    public Event patchEvent(Long eventId, Event partialEventDetails) {
        logger.info("Attempting to patch event with ID: {}", eventId);        Event existingEvent = eventRepo.findById(eventId)
            .orElseThrow(() -> {
                logger.warn("Failed to patch event: Event not found with ID: {}", eventId);
                return new EventNotFoundException(EVENT_NOT_FOUND_MESSAGE + eventId);
            });
        
        logger.debug("Patching event '{}'. Original details: {}", existingEvent.getTitle(), existingEvent);
        logger.debug("Partial details for patch: {}", partialEventDetails);

        if (partialEventDetails.getTitle() != null) existingEvent.setTitle(partialEventDetails.getTitle());
        if (partialEventDetails.getDescription() != null) existingEvent.setDescription(partialEventDetails.getDescription());
        if (partialEventDetails.getDate() != null) existingEvent.setDate(partialEventDetails.getDate());
        if (partialEventDetails.getTime() != null) existingEvent.setTime(partialEventDetails.getTime());
        if (partialEventDetails.getLocation() != null) existingEvent.setLocation(partialEventDetails.getLocation());
        if (partialEventDetails.getCapacity() != null) existingEvent.setCapacity(partialEventDetails.getCapacity());
        if (partialEventDetails.getCategory() != null) existingEvent.setCategory(partialEventDetails.getCategory());
        
        Event patchedEvent = eventRepo.save(existingEvent);
        logger.info("Successfully patched event with ID: {}. Current title: '{}'", patchedEvent.getEventId(), patchedEvent.getTitle());
        return patchedEvent;
    }    @Override
    @Transactional
    public boolean deleteEvent(Long eventId) {
        logger.info("Attempting to delete event with ID: {}", eventId);        if (!eventRepo.existsById(eventId)) {
            logger.warn("Failed to delete event: Event not found with ID: {}", eventId);
            throw new EventNotFoundException(EVENT_NOT_FOUND_MESSAGE + eventId);
        }
        eventRepo.deleteById(eventId);
        logger.info("Successfully deleted event with ID: {}", eventId);
        return true;
    }
}