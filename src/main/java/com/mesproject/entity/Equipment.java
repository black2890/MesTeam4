package com.mesproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Equipment {

    @Id
    @GeneratedValue
    private Long equipmentId;
    private String equipmentName;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private Process processId;
    private String product;

    private Long productionCapacity;
    private String equipmentStatus;
    private LocalDateTime setupTime;
    private Long cycleHour;
    private LocalDate acquisitionDate;
}
