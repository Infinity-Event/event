package com.capgemini.event;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.capgemini.event.controllers.FeedbackRestController;
import com.capgemini.event.entities.Feedback;
import com.capgemini.event.services.FeedbackService;

import jakarta.persistence.EntityNotFoundException;

public class FeedbackRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private FeedbackService feedbackService;

    @InjectMocks
    private FeedbackRestController feedbackRestController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(feedbackRestController).build();
    }

    @Test
    public void testGetAllFeedbacks() throws Exception {
        Feedback feedback1 = new Feedback(1L, 5, "Great event!", null, null);
        Feedback feedback2 = new Feedback(2L, 4, "Good event, could improve.", null, null);
        
        when(feedbackService.getAllFeedbacks()).thenReturn(Arrays.asList(feedback1, feedback2));

        mockMvc.perform(get("/api/feedbacks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].feedbackId").value(1L))
                .andExpect(jsonPath("$[1].feedbackId").value(2L));
        
        verify(feedbackService, times(1)).getAllFeedbacks();
    }

    @Test
    public void testGetFeedbackById() throws Exception {
        Feedback feedback = new Feedback(1L, 5, "Great event!", null, null);
        
        when(feedbackService.getFeedbackById(1L)).thenReturn(feedback);

        mockMvc.perform(get("/api/feedbacks/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedbackId").value(1L))
                .andExpect(jsonPath("$.review").value("Great event!"));

        verify(feedbackService, times(1)).getFeedbackById(1L);
    }

    @Test
    public void testGetFeedbackById_NotFound() throws Exception {
        when(feedbackService.getFeedbackById(1L)).thenThrow(new EntityNotFoundException("Feedback not found"));

        mockMvc.perform(get("/api/feedbacks/{id}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Feedback not found"));

        verify(feedbackService, times(1)).getFeedbackById(1L);
    }

    @Test
    public void testCreateFeedback() throws Exception {
        Feedback feedback = new Feedback(1L, 5, "Great event!", null, null);
        
        when(feedbackService.createFeedback(any(Feedback.class))).thenReturn(feedback);

        mockMvc.perform(post("/api/feedbacks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"rating\":5, \"review\":\"Great event!\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/feedbacks/1"))
                .andExpect(jsonPath("$.feedbackId").value(1L))
                .andExpect(jsonPath("$.review").value("Great event!"));

        verify(feedbackService, times(1)).createFeedback(any(Feedback.class));
    }

    @Test
    public void testUpdateFeedback() throws Exception {
        Feedback existingFeedback = new Feedback(1L, 4, "Good event.", null, null);
        Feedback updatedFeedback = new Feedback(1L, 5, "Great event!", null, null);

        when(feedbackService.updateFeedback(1L, updatedFeedback)).thenReturn(updatedFeedback);

        mockMvc.perform(put("/api/feedbacks/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"rating\":5, \"review\":\"Great event!\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value(5))
                .andExpect(jsonPath("$.review").value("Great event!"));

        verify(feedbackService, times(1)).updateFeedback(1L, updatedFeedback);
    }

    @Test
    public void testPatchFeedback() throws Exception {
        Feedback existingFeedback = new Feedback(1L, 4, "Good event.", null, null);
        Feedback patchedFeedback = new Feedback(1L, 5, "Excellent event!", null, null);

        when(feedbackService.patchFeedback(1L, patchedFeedback)).thenReturn(patchedFeedback);

        mockMvc.perform(patch("/api/feedbacks/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"rating\":5, \"review\":\"Excellent event!\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value(5))
                .andExpect(jsonPath("$.review").value("Excellent event!"));

        verify(feedbackService, times(1)).patchFeedback(1L, patchedFeedback);
    }

    @Test
    public void testDeleteFeedback() throws Exception {
        doNothing().when(feedbackService).deleteFeedback(1L);

        mockMvc.perform(delete("/api/feedbacks/{id}", 1L))
                .andExpect(status().isOk());

        verify(feedbackService, times(1)).deleteFeedback(1L);
    }
}
