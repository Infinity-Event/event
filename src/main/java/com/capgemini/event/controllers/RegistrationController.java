package com.capgemini.event.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.capgemini.event.entities.Event;
import com.capgemini.event.entities.Registration;
import com.capgemini.event.entities.User;
import com.capgemini.event.services.RegistrationService;

@RestController
@RequestMapping("/api/register")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    
    @PostMapping
    public Registration registerUserToEvent(@RequestBody Registration registrationRequest) {
        User user = registrationRequest.getUser();
        Event event = registrationRequest.getEvent();

        return registrationService.registerUserToEvent(user, event);
    }

    
    @PostMapping("/user")
    public List<Registration> getRegistrationsByUser(@RequestBody User user) {
        return registrationService.getRegistrationsByUser(user);
    }
    
}
