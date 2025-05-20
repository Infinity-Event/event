package com.capgemini.event.services;

import com.capgemini.event.dto.MyRegistrationDto;
import com.capgemini.event.dto.PastEventDto;
import com.capgemini.event.dto.RegistrationCountDto;
import com.capgemini.event.entities.Registration;

import java.util.List;

public interface RegistrationService {

	Registration createRegistration(Registration registration, Long userId, Long eventId);

	Registration getRegistrationById(Long regId);

	List<Registration> getRegistrationByUserId(Long userId);

	List<Registration> getAllRegistrations();

	Registration updateRegistration(Long regId, Registration registrationDetails);

	boolean deleteRegistration(Long regId);

	List<Registration> getRegistrationsByUserId(Long userId);

	List<Registration> getRegistrationsByEventId(Long eventId);

	List<PastEventDto> getPastEventRegistration(Long eventId);

	List<MyRegistrationDto> getMyRegistration(Long userId);

	List<RegistrationCountDto> getRegistrationCountPerEvent();
}