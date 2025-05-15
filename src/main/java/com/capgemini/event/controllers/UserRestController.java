package com.capgemini.event.controllers;

import com.capgemini.event.entities.User;
import com.capgemini.event.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

	@Autowired
	private UserService userService;

	@GetMapping
	public ResponseEntity<List<User>> getAllUsers() {
		return ResponseEntity.ok(userService.getAllUsers());
	}

	@GetMapping("/{id}")
	public ResponseEntity<User> getUserById(@PathVariable Long id) {
		return ResponseEntity.ok(userService.getUserById(id));
	}

	@PostMapping
	public ResponseEntity<User> createUser(@Valid @RequestBody User user, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new IllegalArgumentException(bindingResult.getFieldErrors().toString());
		}
		User createdUser = userService.createUser(user);
		return ResponseEntity.created(URI.create("/api/users/" + createdUser.getUserId())).body(createdUser);
	}

	@PutMapping("/{id}")
	public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User user) {
		User updatedUser = userService.updateUser(id, user);
		return ResponseEntity.ok(updatedUser);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
		userService.deleteUser(id);
		return ResponseEntity.ok().build();
	}

//    end
}
