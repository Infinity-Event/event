package com.capgemini.event.services;

import java.util.List;


import com.capgemini.event.entities.Event;
import com.capgemini.event.entities.Registration;
import com.capgemini.event.entities.User;

public interface RegistrationService {

	Registration registerUserToEvent(User user, Event event);

	List<Registration> getRegistrationsByUser(User user);

	List<Registration> getRegistrationsByEvent(Event event);

	boolean isUserAlreadyRegistered(User user, Event event);

	void deleteRegistration(Long registrationId);
}
