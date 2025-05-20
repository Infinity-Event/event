package com.capgemini.event.services;

import com.capgemini.event.dto.MyRegistrationDto;
import com.capgemini.event.dto.PastEventDto;
import com.capgemini.event.dto.RegistrationCountDto;
import com.capgemini.event.entities.Event;
import com.capgemini.event.entities.Registration;
import com.capgemini.event.entities.User;
import com.capgemini.event.exceptions.UserNotFoundException;
import com.capgemini.event.repositories.EventRepo;
import com.capgemini.event.repositories.RegistrationRepo;
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
public class RegistrationServiceImpl implements RegistrationService {

	private final RegistrationRepo registrationRepo;
	private final UserRepo userRepo;
	private final EventRepo eventRepo;

	@Override
	@Transactional
	public Registration createRegistration(Registration registration, Long userId, Long eventId) {
		log.info("Creating registration for user ID {} and event ID {}", userId, eventId);
		User user = userRepo.findById(userId).orElseThrow(() -> {
			log.warn("User not found with ID: {}", userId);
			return new UserNotFoundException("User Not Found!");
		});

		Event event = eventRepo.findById(eventId).orElseThrow(() -> {
			log.warn("Event not found with ID: {}", eventId);
			return new RuntimeException("Event Not Found!");
		});

		registration.setUser(user);
		registration.setEvent(event);

		log.debug("Saving registration: {}", registration);
		Registration saved = registrationRepo.save(registration);
		log.info("Registration created with ID: {}", saved.getRegId());
		return saved;
	}

	@Override
	public Registration getRegistrationById(Long regId) {
		log.info("Retrieving registration by ID: {}", regId);
		return registrationRepo.findById(regId).orElseThrow(() -> {
			log.warn("Registration not found with ID: {}", regId);
			return new RuntimeException("Registration not found with ID: " + regId);
		});
	}

	@Override
	public List<Registration> getAllRegistrations() {
		log.info("Fetching all registrations");
		List<Registration> registrations = registrationRepo.findAll();
		log.debug("Total registrations found: {}", registrations.size());
		return registrations;
	}

	@Override
	@Transactional
	public Registration updateRegistration(Long regId, Registration registrationDetails) {
		log.info("Updating registration with ID: {}", regId);
		Optional<Registration> existingRegistrationOptional = registrationRepo.findById(regId);

		if (existingRegistrationOptional.isEmpty()) {
			log.warn("Cannot update. Registration not found with ID: {}", regId);
			return null;
		}

		Registration existingRegistration = existingRegistrationOptional.get();

		if (registrationDetails.getRegDate() != null) {
			log.debug("Updating registration date from {} to {}", existingRegistration.getRegDate(),
					registrationDetails.getRegDate());
			existingRegistration.setRegDate(registrationDetails.getRegDate());
		}

		Registration updated = registrationRepo.save(existingRegistration);
		log.info("Registration updated for ID: {}", regId);
		return updated;
	}

	@Override
	@Transactional
	public boolean deleteRegistration(Long regId) {
		log.info("Attempting to delete registration with ID: {}", regId);
		if (!registrationRepo.existsById(regId)) {
			log.warn("Cannot delete. Registration not found with ID: {}", regId);
			return false;
		}
		registrationRepo.deleteById(regId);
		log.info("Registration deleted with ID: {}", regId);
		return true;
	}

	@Override
	public List<Registration> getRegistrationsByUserId(Long userId) {
		log.info("Fetching registrations for user ID: {}", userId);
		Optional<User> userOptional = userRepo.findById(userId);
		if (userOptional.isEmpty()) {
			log.warn("User not found with ID: {}", userId);
			return Collections.emptyList();
		}
		List<Registration> registrations = registrationRepo.findByUser(userOptional.get());
		log.debug("Found {} registrations for user ID: {}", registrations.size(), userId);
		return registrations;
	}

	@Override
	public List<Registration> getRegistrationsByEventId(Long eventId) {
		log.info("Fetching registrations for event ID: {}", eventId);
		Optional<Event> eventOptional = eventRepo.findById(eventId);
		if (eventOptional.isEmpty()) {
			log.warn("Event not found with ID: {}", eventId);
			return Collections.emptyList();
		}
		List<Registration> registrations = registrationRepo.findByEvent(eventOptional.get());
		log.debug("Found {} registrations for event ID: {}", registrations.size(), eventId);
		return registrations;
	}

	@Override
	public List<Registration> getRegistrationByUserId(Long userId) {
		log.info("Fetching registrations for user ID: {}", userId);
		Optional<User> userOptional = userRepo.findById(userId);
		if (userOptional.isEmpty()) {
			log.warn("User not found with ID: {}", userId);
			throw new UserNotFoundException("User Not Found!");
		}
		return registrationRepo.findByUserUserId(userId);
	}

	@Override
	public List<PastEventDto> getPastEventRegistration(Long userId) {
		log.info("Fetching past event registrations for User ID: {}", userId);
		Optional<User> userOptional = userRepo.findById(userId);
		if (!userOptional.isPresent()) {
			log.warn("User not found with ID: {}", userId);
			throw new UserNotFoundException("User Not Found!");
		}
		return registrationRepo.findPastRegistrations(userId);
	}

	@Override
	public List<MyRegistrationDto> getMyRegistration(Long userId) {
		log.info("Fetching my events registrations for User ID: {}", userId);
		Optional<User> userOptional = userRepo.findById(userId);
		if (!userOptional.isPresent()) {
			log.warn("User not found with ID: {}", userId);
			throw new UserNotFoundException("User Not Found!");
		}
		return registrationRepo.findMyRegistrations(userId);
	}

	@Override
	public List<RegistrationCountDto> getRegistrationCountPerEvent() {
		log.info("Fetching events details as per count of registration");
		return registrationRepo.getRegistrationCountPerEvent();
	}
}
