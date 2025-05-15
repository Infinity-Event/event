package com.capgemini.event.services;


import com.capgemini.event.entities.User;
import com.capgemini.event.exceptions.UserNotFoundException;
import com.capgemini.event.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, User updatedUser) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());
            user.setPassword(updatedUser.getPassword());
            user.setPhone(updatedUser.getPhone());
            user.setType(updatedUser.getType());
            return userRepository.save(user);
        }
        return null;
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
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
				.orElseThrow(()->new UserNotFoundException("Username or Email not Found !"));
    }
    
//    end

	
}