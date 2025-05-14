package com.capgemini.event.service.impl;

import com.capgemini.event.entities.Event;
import com.capgemini.event.entities.User;
import com.capgemini.event.entities.UserType;
import com.capgemini.event.repositories.EventRepo;
import com.capgemini.event.repositories.UserRepo;
import com.capgemini.event.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public List<Event> getUpcomingEventsByOrganizer(Long organizerId) {
        Optional<User> organizerOptional = userRepo.findById(organizerId);
        if (organizerOptional.isEmpty()) {
           return Collections.emptyList();
        }
        return eventRepo.findUpcomingByOrganizer(organizerOptional.get(), LocalDate.now(), LocalTime.now());
    }

    @Override
    public List<Event> getPastEventsByOrganizer(Long organizerId) {
        Optional<User> organizerOptional = userRepo.findById(organizerId);
         if (organizerOptional.isEmpty()) {
           return Collections.emptyList();
        }
        return eventRepo.findPastByOrganizer(organizerOptional.get(), LocalDate.now(), LocalTime.now());
    }
    
    @Override
    public List<Event> getOngoingEventsByOrganizer(Long organizerId) {
        Optional<User> organizerOptional = userRepo.findById(organizerId);
        if (organizerOptional.isEmpty()) {
           return Collections.emptyList();
        }
        return eventRepo.findByOrganizerAndDateOrderByTimeAsc(organizerOptional.get(), LocalDate.now());
    }

    @Override
    @Transactional
    public Event updateEvent(Long eventId, Event eventDetails, Long currentUserId) {
        Optional<Event> existingEventOptional = eventRepo.findById(eventId);
        if (existingEventOptional.isEmpty()) {
            return null;
        }
        Event existingEvent = existingEventOptional.get();

        Optional<User> currentUserOptional = userRepo.findById(currentUserId);
        if (currentUserOptional.isEmpty()) {
            return null; 
        }
        User currentUser = currentUserOptional.get();
        
        if (!existingEvent.getOrganizer().getUserId().equals(currentUserId) && currentUser.getType() != UserType.ADMIN) {
            return existingEvent; // Or null, to indicate unauthorized and no change
        }

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
    @Transactional
    public boolean deleteEvent(Long eventId, Long currentUserId) {
        Optional<Event> eventOptional = eventRepo.findById(eventId);
        if (eventOptional.isEmpty()) {
            return false;
        }
        Event event = eventOptional.get();

        Optional<User> currentUserOptional = userRepo.findById(currentUserId);
         if (currentUserOptional.isEmpty()) {
            return false;
        }
        User currentUser = currentUserOptional.get();

        if (!event.getOrganizer().getUserId().equals(currentUserId) && currentUser.getType() != UserType.ADMIN) {
            return false; 
        }
        
        eventRepo.delete(event);
        return true;
    }

    @Override
    public List<Event> getUpcomingEvents() {
        return eventRepo.findAllUpcoming(LocalDate.now(), LocalTime.now());
    }

    @Override
    public List<Event> getPastEvents() {
         return eventRepo.findAllPast(LocalDate.now(), LocalTime.now());
    }
    
    //added this method to get trending events

    @Override
    public List<Event> getTrendingEvents() {
        List<Event> allEvents = eventRepo.findAll();
        if (allEvents.isEmpty()) {
            return Collections.emptyList();
        }
        Collections.shuffle(allEvents); 
        return allEvents.stream().limit(5).collect(Collectors.toList());
    }
}