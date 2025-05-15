package com.capgemini.event.user;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.capgemini.event.controllers.UserRestController;
import com.capgemini.event.entities.User;
import com.capgemini.event.entities.UserType;
import com.capgemini.event.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(UserRestController.class)
class UserRestControllerTest {


	@Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = new User(1L, "Alice", "alice@example.com", "password123", "1234567890", UserType.ADMIN);
        user2 = new User(2L, "Bob", "bob@example.com", "password456", "1234567890", UserType.NORMAL);
    }


    @Test
    void testGetAllUsers() throws Exception {
        Mockito.when(userService.getAllUsers()).thenReturn(Arrays.asList(user1, user2));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Alice"))
                .andExpect(jsonPath("$[1].name").value("Bob"));
    }

    @Test
    void testGetUserById() throws Exception {
        Mockito.when(userService.getUserById(1L)).thenReturn(user1);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice"));
    }

    @Test
    void testCreateUser() throws Exception {
        Mockito.when(userService.createUser(any(User.class))).thenReturn(user1);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(user1)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/users/1"))
                .andExpect(jsonPath("$.name").value("Alice"));
    }

    @Test
    void testUpdateUser() throws Exception {
        Mockito.when(userService.updateUser(eq(1L), any(User.class))).thenReturn(user1);

        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(user1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice"));
    }

    @Test
    void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk());

        Mockito.verify(userService).deleteUser(1L);
    }
}

// end