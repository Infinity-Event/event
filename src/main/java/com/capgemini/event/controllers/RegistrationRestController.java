package com.capgemini.event.controllers;

import com.capgemini.event.dto.MyRegistrationDto;
import com.capgemini.event.dto.PastEventDto;
import com.capgemini.event.dto.RegistrationCountDto;
import com.capgemini.event.entities.Registration;
import com.capgemini.event.services.RegistrationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/registrations")
public class RegistrationRestController {

	private RegistrationService registrationService;

	@Autowired
	public RegistrationRestController(RegistrationService registrationService) {
		this.registrationService = registrationService;
	}

	@GetMapping
	public ResponseEntity<List<Registration>> getAllRegistrations() {
		log.info("Received request to fetch all registrations");
		List<Registration> registrations = registrationService.getAllRegistrations();
		log.debug("Returning {} registrations", registrations.size());
		return ResponseEntity.ok(registrations);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Registration> getRegistrationById(@PathVariable Long id) {
		log.info("Received request to fetch registration with ID: {}", id);
		Registration registration = registrationService.getRegistrationById(id);
		log.debug("Registration fetched: {}", registration);
		if (registration == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(registration);
	}

	@GetMapping("/users/{userId}")
	public ResponseEntity<List<Registration>> getRegistrationByUserId(@PathVariable Long userId) {
		log.info("Received request to fetch registrations for user ID: {}", userId);
		List<Registration> registrations = registrationService.getRegistrationByUserId(userId);

		log.debug("Returning {} registrations", registrations.size());
		return ResponseEntity.ok(registrations);
	}

	@GetMapping("/my-events/users/{userId}")
	public ResponseEntity<List<MyRegistrationDto>> getMyRegistrations(@PathVariable Long userId) {
		log.info("Received request to fetch registrations for user ID: {}", userId);
		List<MyRegistrationDto> registrations = registrationService.getMyRegistration(userId);

		log.debug("Returning {} registrations", registrations.size());
		return ResponseEntity.ok(registrations);
	}

	@GetMapping("/events/users/{userId}")
	public ResponseEntity<List<PastEventDto>> getPastEventRegistration(@PathVariable Long userId) {
		log.info("Received request to fetch registrations for user ID: {}", userId);
		List<PastEventDto> registrations = registrationService.getPastEventRegistration(userId);
		log.debug("Returning {} registrations", registrations.size());
		return ResponseEntity.ok(registrations);
	}

	@GetMapping("/count")
	public ResponseEntity<List<RegistrationCountDto>> getRegistrationCountPerEvent() {
		log.info("Received request to fetch registration count per event");
		List<RegistrationCountDto> registrations = registrationService.getRegistrationCountPerEvent();
		return ResponseEntity.ok(registrations);
	}

	@PostMapping("event/{eventId}/user/{userId}")
	public ResponseEntity<Registration> createRegistration(@Valid @RequestBody Registration registration,
			BindingResult bindingResult, @PathVariable Long eventId, @PathVariable Long userId) {

		log.info("Received request to create registration: {}", registration);
		if (bindingResult.hasErrors()) {
			throw new IllegalArgumentException(bindingResult.getFieldErrors().toString());
		}
		Registration createdRegistration = registrationService.createRegistration(registration, userId, eventId);
		if (createdRegistration == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		log.debug("Registration created with ID: {}", createdRegistration.getRegId());
		return ResponseEntity.created(URI.create("/api/registrations/" + createdRegistration.getRegId()))
				.body(createdRegistration);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Registration> updateRegistration(@PathVariable Long id,
			@Valid @RequestBody Registration registrationDetails) {
		Registration existingRegistration = registrationService.getRegistrationById(id);
		if (existingRegistration == null) {
			return ResponseEntity.notFound().build();
		}
		Registration updatedRegistration = registrationService.updateRegistration(id, registrationDetails);
		if (updatedRegistration == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		return ResponseEntity.ok(updatedRegistration);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteRegistration(@PathVariable Long id) {
		log.info("Received request to delete registration with ID: {}", id);
		boolean deleted = registrationService.deleteRegistration(id);
		if (!deleted) {
			return ResponseEntity.notFound().build();
		}
		log.info("Registration with ID {} successfully deleted", id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<List<Registration>> getRegistrationsByUserId(@PathVariable Long userId) {
		List<Registration> registrations = registrationService.getRegistrationsByUserId(userId);
		return ResponseEntity.ok(registrations);
	}

	@GetMapping("/event/{eventId}")
	public ResponseEntity<List<Registration>> getRegistrationsByEventId(@PathVariable Long eventId) {
		List<Registration> registrations = registrationService.getRegistrationsByEventId(eventId);
		return ResponseEntity.ok(registrations);
	}
}
