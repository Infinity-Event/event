package com.capgemini.event.services;

import com.capgemini.event.entities.Event;
import com.capgemini.event.entities.Registration;
import com.capgemini.event.entities.User;
import com.capgemini.event.exceptions.UserNotFoundException;
import com.capgemini.event.repositories.EventRepo;
import com.capgemini.event.repositories.RegistrationRepo;
import com.capgemini.event.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class RegistrationServiceImpl implements RegistrationService {

	private final RegistrationRepo registrationRepo;
	private final UserRepo userRepo;
	private final EventRepo eventRepo;

	@Autowired
	public RegistrationServiceImpl(RegistrationRepo registrationRepo, UserRepo userRepo, EventRepo eventRepo) {
		this.registrationRepo = registrationRepo;
		this.userRepo = userRepo;
		this.eventRepo = eventRepo;
	}

	@Override
	@Transactional
	public Registration createRegistration(Registration registration, Long userId, Long eventId) {
		User user = userRepo.findById(userId).orElseThrow(()-> new UserNotFoundException("User Not Found!"));
		Event event  = eventRepo.findById(eventId).orElseThrow(null);
		registration.setEvent(event);
		registration.setUser(user);

		return registrationRepo.save(registration);
	}

	@Override
	public Registration getRegistrationById(Long regId) {
		return registrationRepo.findById(regId).orElse(null);
	}

	@Override
	public List<Registration> getAllRegistrations() {
		return registrationRepo.findAll();
	}

	@Override
	@Transactional
	public Registration updateRegistration(Long regId, Registration registrationDetails) {
		Optional<Registration> existingRegistrationOptional = registrationRepo.findById(regId);
		if (existingRegistrationOptional.isEmpty()) {
			return null;
		}
		Registration existingRegistration = existingRegistrationOptional.get();

		if (registrationDetails.getRegDate() != null) {
			existingRegistration.setRegDate(registrationDetails.getRegDate());
		}

		return registrationRepo.save(existingRegistration);
	}

	@Override
	@Transactional
	public boolean deleteRegistration(Long regId) {
		if (!registrationRepo.existsById(regId)) {
			return false;
		}
		registrationRepo.deleteById(regId);
		return true;
	}

	@Override
	public List<Registration> getRegistrationsByUserId(Long userId) {
		Optional<User> userOptional = userRepo.findById(userId);
		if (userOptional.isEmpty()) {
			return Collections.emptyList();
		}
		return registrationRepo.findByUser(userOptional.get());
	}

	@Override
	public List<Registration> getRegistrationsByEventId(Long eventId) {
		Optional<Event> eventOptional = eventRepo.findById(eventId);
		if (eventOptional.isEmpty()) {
			return Collections.emptyList();
		}
		return registrationRepo.findByEvent(eventOptional.get());
	}
}
