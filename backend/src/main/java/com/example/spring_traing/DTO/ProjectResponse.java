package com.example.spring_traing.DTO;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class ProjectResponse {

    private Long id;

    private String name;

    private String description;

    private String goal;

    private String motivation;

    private String status;

    private String priority;

    private LocalDate startDate;

    private LocalDate plannedEndDate;

    private LocalDate actualEndDate;

    private BigDecimal plannedHours;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}