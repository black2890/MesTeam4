package com.mesproject.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class ShipmentDto {
    private Long orderId;
    private String vendorName;
    private LocalDate actualDeliveryDate;

}
