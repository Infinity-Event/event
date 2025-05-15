package com.capgemini.event;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import com.capgemini.event.entities.Category;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import com.capgemini.event.controllers.QueryController;
import com.capgemini.event.entities.Event;
import com.capgemini.event.entities.Query;
import com.capgemini.event.entities.User;
import com.capgemini.event.entities.UserType;
import com.capgemini.event.services.QueryService;

@WebMvcTest(QueryController.class)
class QueryControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private QueryService queryService;

	private Query query1;
	private Query query2;
	private User user;
	private Event event;

	@BeforeEach
	void setUp() {
		
		event = new Event("Tech Talk: AI & Future","AI description",  LocalDate.of(2024, 5, 1),LocalTime.of(10, 0), "Mumbai Hall A", 150,Category.CONFERENCE, null);
		event.setEventId(1L);
		user = new User(1L, "Alice", "alice@example.com", "pass1", "1234567890", UserType.NORMAL);
		query1 = new Query(1L, "Query 1 body", "Open", LocalDate.of(2024, 5, 1), null, user, event);
		query2 = new Query(2L, "Query 2 body", "Closed", LocalDate.of(2024, 5, 2), null, user, event);
	}

	@Test
	void testGetAllQueries() throws Exception {
		List<Query> queries = Arrays.asList(query1, query2);
		when(queryService.getAllQueries()).thenReturn(queries);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/queries")).andExpect(status().isOk())
				.andExpect(jsonPath("$.size()").value(2));
	}

	@Test
    void testGetQueryById() throws Exception {
        when(queryService.getQueryById(1L)).thenReturn(query1);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/queries/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.queryId").value(1))
                .andExpect(jsonPath("$.queryBody").value("Query 1 body"));
    }
	
	 @Test
	    void testCreateQuery() throws Exception {
	        when(queryService.createEventQuery(any(Query.class), eq(1L), eq(1L))).thenReturn(query1);

	        String queryJson = """
	            {
	                "queryId": 1,
	                "queryBody": "Query 1 body",
	                "status": "Open",
	                "queryDate": "2024-05-01",
	                "user": {
	                    "userId": 1,
	                    "name": "Alice",
	                    "email": "alice@example.com",
	                    "password": "pass1",
	                    "phone": "1234567890",
	                    "type": "NORMAL"
	                }
	            }
	        """;
	        mockMvc.perform(MockMvcRequestBuilders.post("/api/queries/event/1/user/1") 
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(queryJson))
	            .andExpect(status().isCreated())
	            .andExpect(jsonPath("$.queryBody").value("Query 1 body"));
	    }

	    @Test
	    void testDeleteQuery() throws Exception {
	        doNothing().when(queryService).deleteQuery(1L);
	        mockMvc.perform(MockMvcRequestBuilders.delete("/api/queries/1"))
	                .andExpect(status().isNoContent());
	        verify(queryService, times(1)).deleteQuery(1L);
	    }
}