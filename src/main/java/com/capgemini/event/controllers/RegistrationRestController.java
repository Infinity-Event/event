package com.capgemini.event.controllers;

import com.capgemini.event.entities.Registration;
import com.capgemini.event.services.RegistrationService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/registrations")
public class RegistrationRestController {

	private RegistrationService registrationService;

	@Autowired
	public RegistrationRestController(RegistrationService registrationService) {
		this.registrationService = registrationService;
	}
	
	@GetMapping
	public ResponseEntity<List<Registration>> getAllRegistrations() {
		List<Registration> registrations = registrationService.getAllRegistrations();
		return ResponseEntity.ok(registrations);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Registration> getRegistrationById(@PathVariable Long id) {
		Registration registration = registrationService.getRegistrationById(id);
		if (registration == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(registration);
	}

	@PostMapping("event/{eventId}/user/{userId}")
	public ResponseEntity<Registration> createRegistration(@Valid @RequestBody Registration registration,
			BindingResult bindingResult, @PathVariable Long eventId, @PathVariable Long userId) {
		if (bindingResult.hasErrors()) {
			throw new IllegalArgumentException(bindingResult.getFieldErrors().toString());
		}
		Registration createdRegistration = registrationService.createRegistration(registration, userId, eventId);
		if (createdRegistration == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
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
		boolean deleted = registrationService.deleteRegistration(id);
		if (!deleted) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok().build();
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
