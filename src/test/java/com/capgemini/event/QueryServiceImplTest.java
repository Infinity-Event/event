package com.capgemini.event;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.capgemini.event.entities.Category;
import com.capgemini.event.entities.Event;
import com.capgemini.event.entities.Query;
import com.capgemini.event.entities.User;
import com.capgemini.event.entities.UserType;
import com.capgemini.event.exceptions.QueryNotFoundException;
import com.capgemini.event.exceptions.UserNotFoundException;
import com.capgemini.event.repositories.EventRepo;
import com.capgemini.event.repositories.QueryRepo;
import com.capgemini.event.repositories.UserRepo;
import com.capgemini.event.services.QueryServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class QueryServiceImplTest {

    @Mock
    private QueryRepo queryRepo;

    @Mock
    private UserRepo userRepo;
    
    @Mock
    private EventRepo eventRepo;

    @InjectMocks
    private QueryServiceImpl queryService;

    private User user;
    private Query query1, query2;
    private Event event;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        event = new Event("Tech Talk: AI & Future","AI description",  LocalDate.of(2024, 5, 1),LocalTime.of(10, 0), "Mumbai Hall A", 150, Category.CONFERENCE);
		event.setEventId(1L);
        user = new User(1L, "Alice", "alice@example.com", "pass", "1234567890", UserType.NORMAL);
        query1 = new Query(1L, "Query 1", "Open", LocalDate.now(), null, user, event);
        query2 = new Query(2L, "Query 2", "Closed", LocalDate.now(), null, user, event);
    }

    @Test
    void testGetAllQueries() {
        when(queryRepo.findAll()).thenReturn(Arrays.asList(query1, query2));

        List<Query> result = queryService.getAllQueries();

        assertEquals(2, result.size());
        verify(queryRepo, times(1)).findAll();
    }

    @Test
    void testCreateEventQuery_Success() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(queryRepo.save(any(Query.class))).thenReturn(query1);
        when(eventRepo.findById(1L)).thenReturn(Optional.of(event));

        Query result = queryService.createEventQuery(query1, 1L, 1L);

        assertNotNull(result);
        assertEquals("Query 1", result.getQueryBody());
        verify(userRepo).findById(1L);
        verify(queryRepo).save(query1);
    }

    @Test
    void testCreateEventQuery_UserNotFound() {
        when(userRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            queryService.createEventQuery(query1, 99L, 2L);
        });
    }

    @Test
    void testGetQueryById_Success() {
        when(queryRepo.findById(1L)).thenReturn(Optional.of(query1));

        Query result = queryService.getQueryById(1L);

        assertEquals("Query 1", result.getQueryBody());
        verify(queryRepo).findById(1L);
    }

    @Test
    void testGetQueryById_NotFound() {
        when(queryRepo.findById(999L)).thenReturn(Optional.empty());

        assertThrows(QueryNotFoundException.class, () -> {
            queryService.getQueryById(999L);
        });
    }

    @Test
    void testDeleteQuery_Success() {
        when(queryRepo.findById(1L)).thenReturn(Optional.of(query1));
        doNothing().when(queryRepo).delete(query1);
        queryService.deleteQuery(1L);
        verify(queryRepo).delete(query1);
    }

    @Test
    void testDeleteQuery_NotFound() {
        when(queryRepo.findById(100L)).thenReturn(Optional.empty());

        assertThrows(QueryNotFoundException.class, () -> {
            queryService.deleteQuery(100L);
        });
    }
}