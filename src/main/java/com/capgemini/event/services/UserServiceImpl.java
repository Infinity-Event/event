package com.capgemini.event.services;

import com.capgemini.event.entities.User;
import com.capgemini.event.exceptions.UserNotFoundException;
import com.capgemini.event.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepository;

    @Override
    public List<User> getAllUsers() {
        log.debug("Fetching all users from the repository");
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        log.debug("Fetching user by ID: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> {
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
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User not found for update with ID: {}", id);
                    return new UserNotFoundException("User with ID " + id + " not found");
                });

        user.setName(updatedUser.getName());
        user.setEmail(updatedUser.getEmail());
        user.setPassword(updatedUser.getPassword());
        user.setPhone(updatedUser.getPhone());
        user.setType(updatedUser.getType());

        log.debug("Saving updated user to repository: {}", user);
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
    	User user = userRepository.findById(id).orElseThrow(()->new UserNotFoundException("user not found of Id: "+id));
        log.debug("Attempting to delete user with ID: {}", id);
        if (!userRepository.existsById(id)) {
            log.warn("User not found for deletion with ID: {}", id);
            throw new UserNotFoundException("User with ID " + id + " not found");
        }
        userRepository.deleteById(id);
        log.debug("User with ID {} deleted from repository", id);
    }
}
