package com.capgemini.event.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyRegistrationDto {

    private Long regId;
    private String title;
    private LocalDate date;
    private LocalTime time;
    private String location;
    private String name;
    private LocalDate regDate;

}
