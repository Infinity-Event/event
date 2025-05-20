package com.capgemini.event.services;

import com.capgemini.event.dto.ProfileUpdateDto;
import com.capgemini.event.entities.User;
import java.util.List;

public interface UserService {

  List<User> getAllUsers();

  User getUserById(Long id);

  User createUser(User user);

  User updateUser(Long id, User user);

  void deleteUser(Long id);

  boolean existsByName(String name);

  boolean existsByEmail(String email);

  User findByEmailOrName(String email, String name);

  User editProfile(Long id, ProfileUpdateDto profileUpdateDto);
}