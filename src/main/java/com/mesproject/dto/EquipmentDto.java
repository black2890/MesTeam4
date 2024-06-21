package com.mesproject.dto;

import com.mesproject.constant.EquipmentStatus;
import com.mesproject.constant.ProcessType;
import com.mesproject.entity.Equipment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Getter
@Setter
public class EquipmentDto {

    private Long equipmentId;
    private String equipmentName;
    private ProcessType processType;
    private String Product;
    private String productionCapacity;
    private Long setupTime;
    private Long cycleHour;
    private LocalDateTime acquisitionDate;
    private EquipmentStatus equipmentStatus;

}
