package com.mesproject.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProductionDataDto {
    private String productName;
    private Long totalQuantity;
    private LocalDateTime end;

    // 필드를 모두 초기화하는 생성자
    public ProductionDataDto(String productName, Long totalQuantity, LocalDateTime end) {
        this.productName = productName;
        this.totalQuantity = totalQuantity;
        this.end = end;
    }

}
