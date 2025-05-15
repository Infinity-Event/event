package com.capgemini.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
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

import com.capgemini.event.entities.Event;
import com.capgemini.event.entities.Feedback;
import com.capgemini.event.entities.User;
import com.capgemini.event.repositories.EventRepo;
import com.capgemini.event.repositories.FeedbackRepo;
import com.capgemini.event.repositories.UserRepo;
import com.capgemini.event.services.FeedbackServiceImpl;

import jakarta.persistence.EntityNotFoundException;

class FeedbackServiceImplTest {

    @Mock
    private FeedbackRepo feedbackRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private EventRepo eventRepo;

    @InjectMocks
    private FeedbackServiceImpl feedbackService;

    private Feedback feedback;
    private User user;
    private Event event;

    @BeforeEach //
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setUserId(1L);

        event = new Event();
        event.setEventId(1L);

        feedback = new Feedback();
        feedback.setFeedbackId(1L);
        feedback.setRating(5);
        feedback.setReview("Great event");
        feedback.setUser(user);
        feedback.setEvent(event);
    }

    @Test
    void testGetAllFeedbacks() {
        when(feedbackRepo.findAll()).thenReturn(Arrays.asList(feedback));
        List<Feedback> result = feedbackService.getAllFeedbacks();

        assertEquals(1, result.size());
        verify(feedbackRepo).findAll();
    }

    @Test
    void testGetFeedbackById_Found() {
        when(feedbackRepo.findById(1L)).thenReturn(Optional.of(feedback));
        Feedback result = feedbackService.getFeedbackById(1L);

        assertNotNull(result);
        assertEquals("Great event", result.getReview());
    }

    @Test
    void testGetFeedbackById_NotFound() {
        when(feedbackRepo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> feedbackService.getFeedbackById(99L));
    }

    @Test
    void testCreateFeedback_Success() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(eventRepo.findById(1L)).thenReturn(Optional.of(event));
        when(feedbackRepo.save(feedback)).thenReturn(feedback);

        Feedback result = feedbackService.createFeedback(feedback);
        assertEquals(5, result.getRating());
        verify(feedbackRepo).save(feedback);
    }

    @Test
    void testCreateFeedback_UserNotFound() {
        when(userRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> feedbackService.createFeedback(feedback));
    }

    @Test
    void testUpdateFeedback_Success() {
        Feedback updated = new Feedback();
        updated.setRating(4);
        updated.setReview("Updated review");
        updated.setUser(user);
        updated.setEvent(event);

        when(feedbackRepo.findById(1L)).thenReturn(Optional.of(feedback));
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(eventRepo.findById(1L)).thenReturn(Optional.of(event));
        when(feedbackRepo.save(any(Feedback.class))).thenReturn(feedback);

        Feedback result = feedbackService.updateFeedback(1L, updated);

        assertEquals("Updated review", result.getReview());
        verify(feedbackRepo).save(any(Feedback.class));
    }

    @Test
    void testDeleteFeedback() {
        when(feedbackRepo.findById(1L)).thenReturn(Optional.of(feedback));
        doNothing().when(feedbackRepo).delete(feedback);

        feedbackService.deleteFeedback(1L);

        verify(feedbackRepo).delete(feedback);
    }
}