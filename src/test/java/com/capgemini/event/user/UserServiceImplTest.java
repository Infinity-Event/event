package com.capgemini.event.user;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.capgemini.event.entities.User;
import com.capgemini.event.entities.UserType;
import com.capgemini.event.repositories.UserRepo;
import com.capgemini.event.services.UserServiceImpl;

class UserServiceImplTest {

    @Mock
    private UserRepo userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user1 = new User(1L, "Alice", "alice@example.com", "pass1", "1234567890", UserType.ADMIN);
        user2 = new User(2L, "Bob", "bob@example.com", "pass2", "0987654321", UserType.NORMAL);
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserById_Found() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        User result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals("Alice", result.getName());
    }

    @Test
    void testCreateUser() {
        when(userRepository.save(user1)).thenReturn(user1);

        User result = userService.createUser(user1);

        assertEquals("Alice", result.getName());
        verify(userRepository).save(user1);
    }

    @Test
    void testUpdateUser_Found() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.save(any(User.class))).thenReturn(user1);

        User updated = new User();
        updated.setName("Alice Updated");
        updated.setEmail("alice@new.com");
        updated.setPassword("newpass");
        updated.setPhone("1112223333");
        updated.setType(UserType.ADMIN);

        User result = userService.updateUser(1L, updated);

        assertNotNull(result);
        assertEquals("Alice Updated", result.getName());
        assertEquals("alice@new.com", result.getEmail());
    }
}

