package com.mesproject.dto;

import com.mesproject.constant.MaterialInOutStatus;
import com.mesproject.constant.MaterialOrdersStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Getter
@Setter
public class MaterialInOutDto {

    private Long inOutId;                                // 입고번호
    private String productName;                          // 제품명
    private Long quantity;                               // 수량
    private LocalDateTime expirationDate;                // 유통기한
    private MaterialInOutStatus materialInOutStatus;    // 상태
    private LocalDateTime storageDate;                   // 입고일
    private String storageWorker;                        // 입고자
    private LocalDateTime retrieverDate;                 // 출고일
    private String retrieverWorker;                      // 출고자

}