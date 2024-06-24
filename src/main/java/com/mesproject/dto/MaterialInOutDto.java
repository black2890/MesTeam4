package com.mesproject.dto;

import com.mesproject.constant.MaterialInOutStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Setter
public class MaterialInOutDto {
    private Long inoutId;
    private Long material_order_id;
    private Long work_orders_id;
    private LocalDate expirationDate;
    private MaterialInOutStatus materialInOutStatus;

    private LocalDateTime storageDate;
    private LocalDateTime retrievalDate;
    private String storageWorker;
    private String retrievalWorker;

    private String productName;                          // 제품명
    private Long quantity;                               // 수량
    private LocalDateTime retrieverDate;                 // 출고일
    private String retrieverWorker; 
}
