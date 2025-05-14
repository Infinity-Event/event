package com.capgemini.event.repositories;

import com.capgemini.event.entities.Event;
import com.capgemini.event.entities.Registration;
import com.capgemini.event.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepo extends JpaRepository<Registration, Long> {

	Optional<Registration> findByUserAndEvent(User user, Event event);

	List<Registration> findByUser(User user);

	List<Registration> findByEvent(Event event);

	boolean existsByUserAndEvent(User user, Event event);
}
