package com.capgemini.event;

import com.capgemini.event.controllers.ResponseController;
import com.capgemini.event.entities.Response;
import com.capgemini.event.services.ResponseService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResponseControllerTest {

	@Mock
	private ResponseService responseService;

	@InjectMocks
	private ResponseController responseController;

	private Response sampleResponse() {
		Response response = new Response();
		response.setResponseId(1L);
		response.setResponseBody("Thank you for the event!");
		response.setResponseDate(LocalDate.now());

		return response;
	}

	@Test
	void getAllResponses() {
		Response r1 = sampleResponse();
		Response r2 = sampleResponse();
		r2.setResponseId(2L);
		r2.setResponseBody("Great arrangements!");

		List<Response> expectedResponses = Arrays.asList(r1, r2);
		when(responseService.getAllResponses()).thenReturn(expectedResponses);

		ResponseEntity<List<Response>> response = responseController.getAllResponses();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expectedResponses, response.getBody());
		verify(responseService).getAllResponses();
	}

	@Test
	void getResponseById_Found() {
		Long responseId = 1L;
		Response expected = sampleResponse();
		expected.setResponseId(responseId);

		when(responseService.getResponseById(responseId)).thenReturn(expected);

		ResponseEntity<Response> response = responseController.getResponseById(responseId);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expected, response.getBody());
		verify(responseService).getResponseById(responseId);
	}
	
	@Test
	void getResponseByQueryId_Found() {
		Long queryId = 100L;
		Response expected = sampleResponse();

		when(responseService.getResponseByQueryId(queryId)).thenReturn(expected);

		ResponseEntity<Response> response = responseController.getResponseByQueryId(queryId);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expected, response.getBody());
		verify(responseService).getResponseByQueryId(queryId);
	}

	@Test
	void getResponseByQueryId_NotFound() {
		Long queryId = 999L;

		when(responseService.getResponseByQueryId(queryId)).thenThrow(new RuntimeException("Query not found"));

		ResponseEntity<Response> response = responseController.getResponseByQueryId(queryId);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertNull(response.getBody());
		verify(responseService).getResponseByQueryId(queryId);
	}


	@Test
	void getResponseById_NotFound() {
		Long responseId = 999L;
		when(responseService.getResponseById(responseId)).thenReturn(null);

		ResponseEntity<Response> response = responseController.getResponseById(responseId);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertNull(response.getBody());
		verify(responseService).getResponseById(responseId);
	}


	@Test
	void deleteResponse() {
		Long idToDelete = 5L;
		doNothing().when(responseService).deleteResponse(idToDelete);

		ResponseEntity<Void> response = responseController.deleteResponse(idToDelete);

		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
		verify(responseService).deleteResponse(idToDelete);
	}
}