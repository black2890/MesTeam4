package com.mesproject.dto;

import com.mesproject.constant.OrdersStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class InventoryInDto {
    private Long inventoryId;
    private LocalDateTime storageDate;
    private String storageWorker;

}
