package com.capgemini.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import com.capgemini.event.entities.Category;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.springframework.validation.BindingResult;

import com.capgemini.event.controllers.QueryController;
import com.capgemini.event.entities.Event;
import com.capgemini.event.entities.Query;
import com.capgemini.event.entities.User;
import com.capgemini.event.entities.UserType;
import com.capgemini.event.services.QueryService;

@ExtendWith(MockitoExtension.class)
class QueryControllerTest {

	@Mock
	private QueryService queryService;

	@InjectMocks
	private QueryController queryController;
	
	private Query query1;
	private Query query2;
	private User user;
	private User user1;
	private Event event;

	@BeforeEach
	void setUp() {
		user = new User(1L, "Alice", "alice@example.com", "pass1", "1234567890", UserType.NORMAL);
		user1 = new User(2L, "Alice1", "alice1@example.com", "pass2", "9234567890", UserType.ORGANIZER);
		event = new Event("Tech Talk: AI & Future","AI description",  LocalDate.of(2024, 5, 1),LocalTime.of(10, 0), "Mumbai Hall A", 150, Category.CONFERENCE);
		event.setOrganizer(user1);
		event.setEventId(1L);
		query1 = new Query(1L, "Query 1 body", "Open", LocalDate.of(2024, 5, 1), null, user, event);
		query2 = new Query(2L, "Query 2 body", "Closed", LocalDate.of(2024, 5, 2), null, user, event);
	}

	@Test
	void testGetAllQueries(){
		List<Query> queries = Arrays.asList(query1, query2);
		when(queryService.getAllQueries()).thenReturn(queries);

		ResponseEntity<List<Query>> response = queryController.getAllQueries();
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(queries, response.getBody());
		verify(queryService).getAllQueries();
	}

	@Test
    void testGetQueryById(){
        when(queryService.getQueryById(1L)).thenReturn(query1);

       ResponseEntity<Query> response = queryController.getQueryById(1L);
       assertEquals(HttpStatus.OK, response.getStatusCode());
       assertEquals(query1, response.getBody());
       verify(queryService).getQueryById(1L);
    }
	
	 @Test
	    void testCreateQuery(){
	       when(queryService.createEventQuery(any(Query.class), eq(1L), eq(1L))).thenReturn(query1);
	       BindingResult bindingResult = mock(BindingResult.class);
	       when(bindingResult.hasErrors()).thenReturn(false);
	       ResponseEntity<Query> response = queryController.createQuery(query1,bindingResult,1L,1L);
	       assertEquals(HttpStatus.CREATED, response.getStatusCode());
	       assertEquals(query1, response.getBody());
	       verify(queryService).createEventQuery(query1, 1L, 1L);
	    }

	    @Test
	    void testDeleteQuery(){
	        doNothing().when(queryService).deleteQuery(1L);
	        ResponseEntity<Void> response = queryController.deleteQuery(1L);
	        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	        assertEquals(null, response.getBody());
	        verify(queryService, times(1)).deleteQuery(1L);
	    }
}