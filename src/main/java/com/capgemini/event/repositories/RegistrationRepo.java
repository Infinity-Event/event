package com.capgemini.event.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.capgemini.event.dto.MyRegistrationDto;
import com.capgemini.event.dto.PastEventDto;
import com.capgemini.event.dto.RegistrationCountDto;
import com.capgemini.event.entities.Event;
import com.capgemini.event.entities.Registration;
import com.capgemini.event.entities.User;

@Repository
public interface RegistrationRepo extends JpaRepository<Registration, Long> {

	Optional<Registration> findByUserAndEvent(User user, Event event);

	List<Registration> findByUser(User user);

	List<Registration> findByEvent(Event event);

	long countByEvent(Event event);

	List<Registration> findByUserUserId(Long userId);

	@Query("SELECT new com.capgemini.event.dto.PastEventDto(e.title,e.description,e.date, e.time, e.location, o.name, e.category, r.regDate) "
			+
			"FROM Registration r " +
			"JOIN r.event e " +
			"JOIN e.organizer o " +
			"WHERE r.user.userId = :userId AND e.date < CURRENT_DATE")
	List<PastEventDto> findPastRegistrations(@Param("userId") Long userId);

	@Query("SELECT new com.capgemini.event.dto.MyRegistrationDto(r.regId, e.title, e.date, e.time, e.location, o.name, r.regDate) "
			+
			"FROM Registration r " +
			"JOIN r.event e " +
			"JOIN e.organizer o " +
			"WHERE r.user.userId = :userId ORDER BY e.date")
	List<MyRegistrationDto> findMyRegistrations(@Param("userId") Long userId);

	@Query("SELECT new com.capgemini.event.dto.RegistrationCountDto(e.eventId, COUNT(r)) " +
			"FROM Event e " +
			"LEFT JOIN Registration r ON r.event.eventId = e.eventId " +
			"GROUP BY e.eventId " +
			"ORDER BY COUNT(r) DESC")
	List<RegistrationCountDto> getRegistrationCountPerEvent();

}
