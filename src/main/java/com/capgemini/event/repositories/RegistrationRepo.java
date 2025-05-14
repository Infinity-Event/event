package com.capgemini.event.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capgemini.event.entities.Event;
import com.capgemini.event.entities.Registration;
import com.capgemini.event.entities.User;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {

	Optional<Registration> findByUserAndEvent(User user, Event event);

	List<Registration> findByUser(User user);

	List<Registration> findByEvent(Event event);

	long countByEvent(Event event);
}
