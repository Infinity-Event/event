package com.capgemini.event;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.capgemini.event.controllers.FeedbackRestController;
import com.capgemini.event.entities.Event;
import com.capgemini.event.entities.Feedback;
import com.capgemini.event.entities.User;
import com.capgemini.event.services.FeedbackService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(FeedbackRestController.class)
public class FeedbackRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FeedbackService feedbackService;

    @Autowired
    private ObjectMapper objectMapper;

    private Feedback sampleFeedback() {
        User user = new User(); 
        user.setUserId(1L);

        Event event = new Event(); 
        event.setEventId(1L);
        //

        return new Feedback(1L, 5, "Amazing event, really well managed!", user, event);
    }

    @Test
    public void testGetAllFeedbacks() throws Exception {
        when(feedbackService.getAllFeedbacks()).thenReturn(Arrays.asList(sampleFeedback()));

        mockMvc.perform(get("/api/feedbacks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].feedbackId").value(1))
                .andExpect(jsonPath("$[0].rating").value(5))
                .andExpect(jsonPath("$[0].review").value("Amazing event, really well managed!"));
    }

    @Test
    public void testGetFeedbackById() throws Exception {
        when(feedbackService.getFeedbackById(1L)).thenReturn(sampleFeedback());

        mockMvc.perform(get("/api/feedbacks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedbackId").value(1));
    }

    @Test
    public void testCreateFeedback() throws Exception {
        Feedback feedback = sampleFeedback();
        feedback.setFeedbackId(null); // simulate incoming POST without ID

        Feedback createdFeedback = sampleFeedback();

        when(feedbackService.createFeedback(any(Feedback.class))).thenReturn(createdFeedback);

        mockMvc.perform(post("/api/feedbacks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(feedback)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/feedbacks/1"))
                .andExpect(jsonPath("$.feedbackId").value(1));
    }

    @Test
    public void testUpdateFeedback() throws Exception {
        Feedback updated = sampleFeedback();

        when(feedbackService.updateFeedback(Mockito.eq(1L), any(Feedback.class))).thenReturn(updated);

        mockMvc.perform(put("/api/feedbacks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.review").value("Amazing event, really well managed!"));
    }

    @Test
    public void testPatchFeedback() throws Exception {
        Feedback patched = sampleFeedback();

        when(feedbackService.patchFeedback(Mockito.eq(1L), any(Feedback.class))).thenReturn(patched);

        mockMvc.perform(patch("/api/feedbacks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patched)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value(5));
    }

    @Test
    public void testDeleteFeedback() throws Exception {
        Mockito.doNothing().when(feedbackService).deleteFeedback(1L);

        mockMvc.perform(delete("/api/feedbacks/1"))
                .andExpect(status().isOk());
    }
}