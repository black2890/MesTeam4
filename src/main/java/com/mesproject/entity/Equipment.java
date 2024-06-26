package com.mesproject.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long equipmentId;
    private String equipmentName;
    //private String equipmentType;
    private Long processId;

    //나중에 enum 으로 고쳐야 함
    private String product;
    private Long productionCapacity;
    private String productionCapacityUnit;
    private Long setupTime;
    private Long cycleHour;
    private LocalDateTime acquisitionDate;
    @Enumerated(EnumType.STRING)
    private EquipmentStatus equipmentStatus;

}
