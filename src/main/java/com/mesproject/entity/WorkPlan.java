package com.mesproject.entity;

import jakarta.persistence.*;
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

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Long quantity;
    private LocalDateTime start;
    private LocalDateTime end;

}
