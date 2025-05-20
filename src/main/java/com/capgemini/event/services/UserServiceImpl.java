package com.capgemini.event.services;

import com.capgemini.event.dto.ProfileUpdateDto;
import com.capgemini.event.entities.User;
import com.capgemini.event.exceptions.InvalidPasswordException;
import com.capgemini.event.exceptions.UserNotFoundException;
import com.capgemini.event.repositories.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

	private final UserRepo userRepository;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public UserServiceImpl(UserRepo userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public List<User> getAllUsers() {
		log.debug("Fetching all users from the repository");
		return userRepository.findAll();
	}

	@Override
	public User getUserById(Long id) {
		log.debug("Fetching user by ID: {}", id);
		return userRepository.findById(id).orElseThrow(() -> {
			log.warn("User not found with ID: {}", id);
			return new UserNotFoundException("User with ID " + id + " not found");
		});
	}

	@Override
	public User createUser(User user) {
		log.debug("Saving new user to the repository");
		return userRepository.save(user);
	}

	@Override
	public User updateUser(Long id, User updatedUser) {
		log.debug("Updating user with ID: {}", id);
		return userRepository.findById(id).map(user -> {
			user.setName(updatedUser.getName());
			user.setEmail(updatedUser.getEmail());
			user.setPassword(updatedUser.getPassword());
			user.setPhone(updatedUser.getPhone());
			user.setType(updatedUser.getType());
			log.debug("Saving updated user to repository: {}", user);
			return userRepository.save(user);
		}).orElseThrow(() -> {
			log.warn("User not found for update with ID: {}", id);
			return new UserNotFoundException("User with ID " + id + " not found");
		});
	}

	@Override
	public void deleteUser(Long id) {
		log.debug("Attempting to delete user with ID: {}", id);
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
		userRepository.delete(user);
		log.debug("User with ID {} deleted from repository", id);
	}

	@Override
	public boolean existsByEmail(String email) {

		return userRepository.existsByEmail(email);
	}

	@Override
	public boolean existsByName(String name) {
		return userRepository.existsByName(name);
	}

	@Override
	public User findByEmailOrName(String email, String name) {

		return userRepository.findByEmailOrName(email, name)
				.orElseThrow(() -> new UserNotFoundException("Username or Email not Found !"));
	}

	@Override
	public User editProfile(Long id, ProfileUpdateDto updated) {
		User existing = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));

		if (!passwordEncoder.matches(updated.getOldPassword(), existing.getPassword())) {
			throw new InvalidPasswordException("Invalid password.");
		}
		existing.setName(updated.getName());
		existing.setPhone(updated.getPhone());
		if (updated.getNewPassword() != null && !updated.getNewPassword().isBlank()) {
			existing.setPassword(passwordEncoder.encode(updated.getNewPassword()));
		}
		return userRepository.save(existing);
	}
}
