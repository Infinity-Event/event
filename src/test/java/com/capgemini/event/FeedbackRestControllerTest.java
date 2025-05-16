package com.capgemini.event;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import com.capgemini.event.controllers.FeedbackRestController;
import com.capgemini.event.entities.Feedback;
import com.capgemini.event.services.FeedbackService;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class FeedbackRestControllerTest {

    @Mock
    private FeedbackService feedbackService;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private FeedbackRestController controller;

    @Test
    void getAllFeedbacks_ShouldReturnList() {
        Feedback f1 = new Feedback();
        f1.setFeedbackId(1L);
        Feedback f2 = new Feedback();
        f2.setFeedbackId(2L);
        List<Feedback> feedbacks = Arrays.asList(f1, f2);

        when(feedbackService.getAllFeedbacks()).thenReturn(feedbacks);

        ResponseEntity<List<Feedback>> response = controller.getAllFeedbacks();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
        verify(feedbackService).getAllFeedbacks();
    }

    @Test
    void getFeedbackById_ShouldReturnFeedback() {
        Feedback feedback = new Feedback();
        feedback.setFeedbackId(1L);

        when(feedbackService.getFeedbackById(1L)).thenReturn(feedback);

        ResponseEntity<Feedback> response = controller.getFeedbackById(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getFeedbackId()).isEqualTo(1L);
        verify(feedbackService).getFeedbackById(1L);
    }

    @Test
    void createFeedback_WithValidFeedback_ShouldCreate() {
        Feedback feedback = new Feedback();
        feedback.setFeedbackId(null);
        Feedback createdFeedback = new Feedback();
        createdFeedback.setFeedbackId(10L);

        when(bindingResult.hasErrors()).thenReturn(false);
        when(feedbackService.createFeedback(feedback)).thenReturn(createdFeedback);

        ResponseEntity<Feedback> response = controller.createFeedback(feedback, bindingResult);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getLocation()).isEqualTo(URI.create("/api/feedbacks/10"));
        assertThat(response.getBody().getFeedbackId()).isEqualTo(10L);
        verify(feedbackService).createFeedback(feedback);
    }

    @Test
    void createFeedback_WithValidationErrors_ShouldThrow() {
        Feedback feedback = new Feedback();

        when(bindingResult.hasErrors()).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> {
            controller.createFeedback(feedback, bindingResult);
        });

        verify(feedbackService, never()).createFeedback(any());
    }

    @Test
    void updateFeedback_ShouldReturnUpdated() {
        Feedback feedback = new Feedback();
        feedback.setFeedbackId(1L);
        Feedback updatedFeedback = new Feedback();
        updatedFeedback.setFeedbackId(1L);

        when(feedbackService.updateFeedback(1L, feedback)).thenReturn(updatedFeedback);

        ResponseEntity<Feedback> response = controller.updateFeedback(1L, feedback);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getFeedbackId()).isEqualTo(1L);
        verify(feedbackService).updateFeedback(1L, feedback);
    }

    @Test
    void patchFeedback_ShouldReturnPatched() {
        Feedback feedback = new Feedback();
        feedback.setFeedbackId(1L);
        Feedback patchedFeedback = new Feedback();
        patchedFeedback.setFeedbackId(1L);

        when(feedbackService.patchFeedback(1L, feedback)).thenReturn(patchedFeedback);

        ResponseEntity<Feedback> response = controller.patchFeedback(1L, feedback);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getFeedbackId()).isEqualTo(1L);
        verify(feedbackService).patchFeedback(1L, feedback);
    }

    @Test
    void deleteFeedback_ShouldCallService() {
        doNothing().when(feedbackService).deleteFeedback(1L);

        ResponseEntity<Void> response = controller.deleteFeedback(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(feedbackService).deleteFeedback(1L);
    }
}