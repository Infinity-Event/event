package com.capgemini.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.capgemini.event.entities.Event;
import com.capgemini.event.entities.Feedback;
import com.capgemini.event.entities.User;
import com.capgemini.event.exceptions.FeedbackNotFoundException;
import com.capgemini.event.repositories.FeedbackRepo;
import com.capgemini.event.services.FeedbackServiceImpl;

@ExtendWith(MockitoExtension.class)
class FeedbackServiceImplTest {

    @Mock
    private FeedbackRepo feedbackRepo;

    @InjectMocks
    private FeedbackServiceImpl feedbackService;

    private final Feedback sampleFeedback = new Feedback(1L, 4, "Nice event", new User(), new Event());

    @Test
    void testGetAllFeedbacks() {
        List<Feedback> feedbacks = List.of(sampleFeedback);
        when(feedbackRepo.findAll()).thenReturn(feedbacks);

        List<Feedback> result = feedbackService.getAllFeedbacks();

        assertEquals(1, result.size());
        verify(feedbackRepo, times(1)).findAll();
    }

    @Test
    void testGetFeedbackById_found() {
        when(feedbackRepo.findById(1L)).thenReturn(Optional.of(sampleFeedback));

        Feedback result = feedbackService.getFeedbackById(1L);

        assertNotNull(result);
        assertEquals(sampleFeedback.getFeedbackId(), result.getFeedbackId());
    }

    @Test
    void testGetFeedbackById_notFound() {
        when(feedbackRepo.findById(2L)).thenReturn(Optional.empty());

        assertThrows(FeedbackNotFoundException.class, () -> feedbackService.getFeedbackById(2L));
    }

    @Test
    void testCreateFeedback() {
        when(feedbackRepo.save(sampleFeedback)).thenReturn(sampleFeedback);

        Feedback result = feedbackService.createFeedback(sampleFeedback);

        assertEquals(sampleFeedback, result);
        verify(feedbackRepo).save(sampleFeedback);
    }

    @Test
    void testUpdateFeedback_found() {
        Feedback updated = new Feedback(1L, 5, "Updated", new User(), new Event());
        when(feedbackRepo.findById(1L)).thenReturn(Optional.of(sampleFeedback));
        when(feedbackRepo.save(any())).thenReturn(updated);

        Feedback result = feedbackService.updateFeedback(1L, updated);

        assertEquals("Updated", result.getReview());
        assertEquals(5, result.getRating());
    }

    @Test
    void testUpdateFeedback_notFound() {
        when(feedbackRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(FeedbackNotFoundException.class, () -> feedbackService.updateFeedback(1L, sampleFeedback));
    }

    @Test
    void testPatchFeedback_partialUpdate() {
        Feedback patch = new Feedback(null, "Patched Review");
        when(feedbackRepo.findById(1L)).thenReturn(Optional.of(sampleFeedback));
        when(feedbackRepo.save(any())).thenReturn(sampleFeedback);

        Feedback result = feedbackService.patchFeedback(1L, patch);

        assertEquals("Patched Review", result.getReview());
    }

    @Test
    void testPatchFeedback_notFound() {
        when(feedbackRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(FeedbackNotFoundException.class, () -> feedbackService.patchFeedback(1L, sampleFeedback));
    }

    @Test
    void testDeleteFeedback_success() {
        when(feedbackRepo.existsById(1L)).thenReturn(true);

        feedbackService.deleteFeedback(1L);

        verify(feedbackRepo).deleteById(1L);
    }

    @Test
    void testDeleteFeedback_notFound() {
        when(feedbackRepo.existsById(1L)).thenReturn(false);

        assertThrows(FeedbackNotFoundException.class, () -> feedbackService.deleteFeedback(1L));
    }
}