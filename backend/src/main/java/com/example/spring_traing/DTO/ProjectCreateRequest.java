package com.example.spring_traing.DTO;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ProjectCreateRequest {

    private String name;

    private String description;

    private String goal;

    private String motivation;

    private String status;

    private String priority;

    private LocalDate startDate;

    private LocalDate plannedEndDate;

    private BigDecimal plannedHours;
}