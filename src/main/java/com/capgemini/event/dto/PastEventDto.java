package com.capgemini.event.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.capgemini.event.entities.Category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PastEventDto {
    private String title;
    private String description;
    private LocalDate date;
    private LocalTime time;
    private String location;
    private String name;
    private Category category;
    private LocalDate regDate;

}
