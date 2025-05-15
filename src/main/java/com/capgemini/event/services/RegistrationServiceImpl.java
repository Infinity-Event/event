package com.capgemini.event.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.event.entities.Event;
import com.capgemini.event.entities.Registration;
import com.capgemini.event.entities.User;
import com.capgemini.event.repositories.RegistrationRepo;

@Service
public class RegistrationServiceImpl implements RegistrationService {

	@Autowired
	private RegistrationRepo registrationRepository;

	@Override

	public Registration createRegistration(Registration registration) {
		if (registration.getUser() == null || registration.getUser().getUserId() == null
				|| registration.getEvent() == null || registration.getEvent().getEventId() == null) {
			return null;

		}

		Registration registration = new Registration();
		registration.setUser(user);
		registration.setEvent(event);
		registration.setRegDate(LocalDate.now());
		return registrationRepository.save(registration);
	}

	@Override
	public List<Registration> getRegistrationsByUser(User user) {
		return registrationRepository.findByUser(user);
	}

	@Override
	public List<Registration> getRegistrationsByEvent(Event event) {
		return registrationRepository.findByEvent(event);
	}

	@Override
	public boolean isUserAlreadyRegistered(User user, Event event) {
		return registrationRepository.findByUserAndEvent(user, event).isPresent();
	}

	@Override
	public void deleteRegistration(Long registrationId) {
		registrationRepository.deleteById(registrationId);
	}
}
