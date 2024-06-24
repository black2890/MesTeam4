package com.mesproject.dto;

import com.mesproject.constant.EquipmentStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EquipmentStatusUpdateRequest {
   private List<Long> equipmentIds;
   private List<EquipmentStatus> equipmentStatus;
}
