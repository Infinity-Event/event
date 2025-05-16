package com.capgemini.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.capgemini.event.entities.Event;
import com.capgemini.event.entities.Response;
import com.capgemini.event.repositories.ResponseRepo;
import com.capgemini.event.services.ResponseServiceImpl;

class ResponseServiceImplTest {

	@Mock
	private ResponseRepo responseRepo;

	@InjectMocks
	private ResponseServiceImpl responseService;

	private Response sampleResponse;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		Event event = new Event(); // Assuming a basic constructor or mock object
		event.setEventId(1L); // Assuming Event has an ID field

		sampleResponse = new Response();
		sampleResponse.setResponseId(1L);
		sampleResponse.setResponseBody("This is a response body");
		sampleResponse.setResponseDate(LocalDate.now());
		
	}


	@Test
	void testGetResponseById_Found() {
		when(responseRepo.findById(1L)).thenReturn(Optional.of(sampleResponse));

		Response result = responseService.getResponseById(1L);

		assertNotNull(result);
		assertEquals(1L, result.getResponseId());
		verify(responseRepo).findById(1L);
	}

	@Test
	void testGetResponseById_NotFound() {
		when(responseRepo.findById(100L)).thenReturn(Optional.empty());

		Response result = responseService.getResponseById(100L);

		assertNull(result);
		verify(responseRepo).findById(100L);
	}

	@Test
	void testGetAllResponses() {
		List<Response> responses = List.of(sampleResponse);
		when(responseRepo.findAll()).thenReturn(responses);

		List<Response> result = responseService.getAllResponses();

		assertEquals(1, result.size());
		assertEquals(sampleResponse.getResponseId(), result.get(0).getResponseId());
		verify(responseRepo).findAll();
	}

	@Test
	void testDeleteResponse() {
		Long idToDelete = 1L;
		doNothing().when(responseRepo).deleteById(idToDelete);

		responseService.deleteResponse(idToDelete);

		verify(responseRepo, times(1)).deleteById(idToDelete);
	}
}