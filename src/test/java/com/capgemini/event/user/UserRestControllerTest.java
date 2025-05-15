package com.capgemini.event.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import com.capgemini.event.controllers.UserRestController;
import com.capgemini.event.entities.User;
import com.capgemini.event.entities.UserType;
import com.capgemini.event.services.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

class UserRestControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserRestController userRestController;
    
    @Mock
    private BindingResult bindingResult;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user1 = new User(1L, "Alice", "alice@example.com", "password123", "1234567890", UserType.ADMIN);
        user2 = new User(2L, "Bob", "bob@example.com", "password456", "1234567890", UserType.NORMAL);
    }

    @Test
    void testGetAllUsers() {
        when(userService.getAllUsers()).thenReturn(Arrays.asList(user1, user2));

        ResponseEntity<List<User>> response = userRestController.getAllUsers();

        assertEquals(2, response.getBody().size());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(userService).getAllUsers();
    }

    @Test
    void testGetUserById() {
        when(userService.getUserById(1L)).thenReturn(user1);

        ResponseEntity<User> response = userRestController.getUserById(1L);

        assertEquals("Alice", response.getBody().getName());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(userService).getUserById(1L);
    }

    @Test
    void testCreateUser() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.createUser(user1)).thenReturn(user1);

        ResponseEntity<User> response = userRestController.createUser(user1, bindingResult);

        assertNotNull(response.getBody());
        assertEquals("Alice", response.getBody().getName());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        assertEquals(URI.create("/api/users/1"), response.getHeaders().getLocation());
        verify(userService).createUser(user1);
    }

    @Test
    void testUpdateUser() {
        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(user1);

        ResponseEntity<User> response = userRestController.updateUser(1L, user1);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals("Alice", response.getBody().getName());
        verify(userService).updateUser(1L, user1);
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userService).deleteUser(1L);

        ResponseEntity<Void> response = userRestController.deleteUser(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(userService).deleteUser(1L);
    }
}
