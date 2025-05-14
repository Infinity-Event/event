package com.capgemini.event.services;

import com.capgemini.event.entities.Event;
import com.capgemini.event.entities.User;
import com.capgemini.event.entities.UserType;
import com.capgemini.event.repositories.EventRepo;
import com.capgemini.event.repositories.UserRepo;
import com.capgemini.event.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {

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
        Optional<User> organizerOptional = userRepo.findById(organizerId);
        if (organizerOptional.isEmpty() || organizerOptional.get().getType() != UserType.ORGANIZER) {
            return null;
        }
        event.setOrganizer(organizerOptional.get());
        event.setEventId(null);
        return eventRepo.save(event);
    }

    @Override
    public Event getEventById(Long eventId) {
        return eventRepo.findById(eventId).orElse(null);
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepo.findAll();
    }

    @Override
    public List<Event> getEventsByOrganizer(Long organizerId) {
        Optional<User> organizerOptional = userRepo.findById(organizerId);
        if (organizerOptional.isEmpty()) {
            return Collections.emptyList();
        }
        return eventRepo.findByOrganizer(organizerOptional.get());
    }

    @Override
    public Event updateEvent(Long eventId, Event eventDetails) {
        Optional<Event> eventOptional = eventRepo.findById(eventId);
        if (eventOptional.isEmpty()) {
            return null;
        }
        Event existingEvent = eventOptional.get();
        existingEvent.setTitle(eventDetails.getTitle());
        existingEvent.setDescription(eventDetails.getDescription());
        existingEvent.setDate(eventDetails.getDate());
        existingEvent.setTime(eventDetails.getTime());
        existingEvent.setLocation(eventDetails.getLocation());
        existingEvent.setCapacity(eventDetails.getCapacity());
        existingEvent.setCategory(eventDetails.getCategory());
        return eventRepo.save(existingEvent);
    }

    @Override
    public Event patchEvent(Long eventId, Event partialEventDetails) {
        Optional<Event> eventOptional = eventRepo.findById(eventId);
        if (eventOptional.isEmpty()) {
            return null;
        }
        Event existingEvent = eventOptional.get();
        if (partialEventDetails.getTitle() != null) existingEvent.setTitle(partialEventDetails.getTitle());
        if (partialEventDetails.getDescription() != null) existingEvent.setDescription(partialEventDetails.getDescription());
        if (partialEventDetails.getDate() != null) existingEvent.setDate(partialEventDetails.getDate());
        if (partialEventDetails.getTime() != null) existingEvent.setTime(partialEventDetails.getTime());
        if (partialEventDetails.getLocation() != null) existingEvent.setLocation(partialEventDetails.getLocation());
        if (partialEventDetails.getCapacity() != null) existingEvent.setCapacity(partialEventDetails.getCapacity());
        if (partialEventDetails.getCategory() != null) existingEvent.setCategory(partialEventDetails.getCategory());
        return eventRepo.save(existingEvent);
    }

    @Override
    public boolean deleteEvent(Long eventId) {
        if (!eventRepo.existsById(eventId)) {
            return false;
        }
        eventRepo.deleteById(eventId);
        return true;
    }
}
