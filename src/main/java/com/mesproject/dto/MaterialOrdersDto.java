package com.mesproject.dto;

import com.mesproject.constant.MaterialOrdersStatus;
import com.mesproject.constant.WorkOrdersStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Getter
@Setter
public class MaterialOrdersDto {

    private Long materialOrderId;                       // 발주 번호
    private LocalDateTime materialOrderDate;            // 발주일
    private String productName;                         // 제품명
    private Long quantity;                               // 수량
    private LocalDateTime deliveryDate;                 // 납품일
    private MaterialOrdersStatus materialOrdersStatus;  // 발주 상태

}