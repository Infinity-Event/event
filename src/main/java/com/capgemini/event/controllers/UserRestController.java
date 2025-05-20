package com.capgemini.event.controllers;

import com.capgemini.event.dto.ProfileUpdateDto;
import com.capgemini.event.entities.User;
import com.capgemini.event.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@Slf4j
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("Received request to fetch all users");
        List<User> users = userService.getAllUsers();
        log.debug("Returning {} users", users.size());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        log.info("Received request to fetch user with ID: {}", id);
        User user = userService.getUserById(id);
        log.debug("User fetched: {}", user);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        log.info("Received request to create user: {}", user.getName());
        if (bindingResult.hasErrors()) {
            log.warn("Validation errors while creating user: {}", bindingResult.getFieldErrors());
            throw new IllegalArgumentException(bindingResult.getFieldErrors().toString());
        }
        User createdUser = userService.createUser(user);
        log.debug("User created with ID: {}", createdUser.getUserId());
        return ResponseEntity.created(URI.create("/api/users/" + createdUser.getUserId())).body(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User user) {
        log.info("Received request to update user with ID: {}", id);
        User updatedUser = userService.updateUser(id, user);
        log.debug("User updated: {}", updatedUser);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("Received request to delete user with ID: {}", id);
        userService.deleteUser(id);
        log.info("User with ID {} successfully deleted", id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/edit-profile")
    public ResponseEntity<User> updateUserProfile(@PathVariable Long id, @Valid @RequestBody ProfileUpdateDto newUser) {
        User updated = userService.editProfile(id, newUser);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }
}
