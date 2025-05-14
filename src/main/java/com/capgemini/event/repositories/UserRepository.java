package com.capgemini.event.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.capgemini.event.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
}