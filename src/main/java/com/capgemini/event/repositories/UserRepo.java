package com.capgemini.event.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.capgemini.event.entities.User;

public interface UserRepo extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailOrName(String email, String name);

    Optional<User> findByName(String name);

    boolean existsByName(String name);

    boolean existsByEmail(String email);
}
