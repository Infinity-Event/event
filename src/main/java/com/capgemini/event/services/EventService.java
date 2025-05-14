package com.capgemini.event.services;

import com.capgemini.event.entities.Event;
import java.util.List;

public interface EventService {
    Event createEvent(Event event, Long organizerId);
    Event getEventById(Long eventId);
    List<Event> getAllEvents();
    List<Event> getEventsByOrganizer(Long organizerId);
    List<Event> getUpcomingEventsByOrganizer(Long organizerId);
    List<Event> getPastEventsByOrganizer(Long organizerId);
    List<Event> getOngoingEventsByOrganizer(Long organizerId);
    Event updateEvent(Long eventId, Event eventDetails, Long currentUserId);
    boolean deleteEvent(Long eventId, Long currentUserId);

    List<Event> getUpcomingEvents();
    List<Event> getPastEvents();
    List<Event> getTrendingEvents();
}