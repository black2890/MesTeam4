package com.mesproject.entity;

import com.mesproject.constant.EquipmentStatus;
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
    @JoinColumn(name = "process_id")
    @ManyToOne
    private Process process;
    private String product;
    private Long productionCapacity;
    private String productionCapacityUnit;
    private Long setupTime;
    private Long cycleHour;
    private LocalDateTime acquisitionDate;
    @Enumerated(EnumType.STRING)
    private EquipmentStatus equipmentStatus;

}
