package com.capgemini.event.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.capgemini.event.entities.User;

public interface UserRepo extends JpaRepository<User, Long> {
//	find by email
    User findByEmail(String email);
}