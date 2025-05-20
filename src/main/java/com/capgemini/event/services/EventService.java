package com.capgemini.event.services;

import com.capgemini.event.entities.Event;
import java.util.List;

public interface EventService {
    Event createEvent(Event event, Long organizerId);

    Event getEventById(Long eventId);

    List<Event> getAllEvents();

    Event updateEvent(Long eventId, Event eventDetails);

    Event patchEvent(Long eventId, Event partialEventDetails);

    boolean deleteEvent(Long eventId);

    List<Event> getEventsByOrganizer(Long organizerId);

    List<Event> getEventsByOrganizerEmail(String organizerEmail);
}