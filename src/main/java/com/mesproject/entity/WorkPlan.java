package com.mesproject.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class WorkPlan {
    @Id
    @GeneratedValue
    private Long workPlanId;

    // private Long productId;

    private Long quantity;
    private LocalDateTime start;
    private LocalDateTime end;

}
