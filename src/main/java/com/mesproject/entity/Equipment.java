package com.mesproject.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
    // private String equipmentType;
    private String product;

    private Long productionCapacity;
    private String equipmentStatus;
    private LocalDateTime setupTime;
    private Long cycleHour;
    private LocalDate acquisitionDate;
}
