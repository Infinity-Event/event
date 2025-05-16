package com.capgemini.event;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.capgemini.event.entities.Event;
import com.capgemini.event.entities.Feedback;
import com.capgemini.event.entities.User;
import com.capgemini.event.exceptions.FeedbackNotFoundException;
import com.capgemini.event.repositories.EventRepo;
import com.capgemini.event.repositories.FeedbackRepo;
import com.capgemini.event.repositories.UserRepo;
import com.capgemini.event.services.FeedbackServiceImpl;

public class FeedbackServiceImplTest {

    @InjectMocks
    private FeedbackServiceImpl feedbackService;

    @Mock
    private FeedbackRepo feedbackRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private EventRepo eventRepo;

    private User user;
    private Event event;
    private Feedback feedback;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setUserId(1L);

        event = new Event();
        event.setEventId(1L);

        feedback = new Feedback(1L, 5, "Great event!", user, event);
    }

    @Test
    public void testGetAllFeedbacks() {
        when(feedbackRepo.findAll()).thenReturn(Arrays.asList(feedback));

        var result = feedbackService.getAllFeedbacks();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getRating()).isEqualTo(5);
    }

    @Test
    public void testGetFeedbackById_Found() {
        when(feedbackRepo.findById(1L)).thenReturn(Optional.of(feedback));

        Feedback result = feedbackService.getFeedbackById(1L);

        assertThat(result.getFeedbackId()).isEqualTo(1L);
    }

    @Test
    public void testGetFeedbackById_NotFound() {
        when(feedbackRepo.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> feedbackService.getFeedbackById(1L))
            .isInstanceOf(FeedbackNotFoundException.class);
    }

    @Test
    public void testCreateFeedback() {
        Feedback newFeedback = new Feedback(null, 4, "Nice", user, event);

        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(eventRepo.findById(1L)).thenReturn(Optional.of(event));
        when(feedbackRepo.save(any(Feedback.class))).thenReturn(feedback);

        Feedback result = feedbackService.createFeedback(newFeedback);

        assertThat(result.getRating()).isEqualTo(5); 
    }

    @Test
    public void testUpdateFeedback_Found() {
        Feedback updatedFeedback = new Feedback(1L, 3, "Updated", user, event);

        when(feedbackRepo.findById(1L)).thenReturn(Optional.of(feedback));
        when(feedbackRepo.save(any(Feedback.class))).thenReturn(updatedFeedback);

        Feedback result = feedbackService.updateFeedback(1L, updatedFeedback);

        assertThat(result.getRating()).isEqualTo(3);
    }

    @Test
    public void testUpdateFeedback_NotFound() {
        when(feedbackRepo.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> feedbackService.updateFeedback(1L, feedback))
            .isInstanceOf(FeedbackNotFoundException.class);
    }

    @Test
    public void testPatchFeedback() {
        Feedback patch = new Feedback();
        patch.setRating(2); // Only patching rating

        when(feedbackRepo.findById(1L)).thenReturn(Optional.of(feedback));
        when(feedbackRepo.save(any(Feedback.class))).thenReturn(feedback);

        Feedback result = feedbackService.patchFeedback(1L, patch);

        assertThat(result.getRating()).isEqualTo(2);
    }

    @Test
    public void testDeleteFeedback() {
        when(feedbackRepo.findById(1L)).thenReturn(Optional.of(feedback));
        doNothing().when(feedbackRepo).deleteById(1L);

        feedbackService.deleteFeedback(1L);

        verify(feedbackRepo, times(1)).deleteById(1L);
    }
    
}
