package com.mesproject.dto;

import com.mesproject.entity.WorkPlan;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class WorkPlanDto {

    private Long workPlanId;
    private Long productId;
    private String productName;
    private Long quantity;
    private LocalDateTime start;
    private LocalDateTime end;
    private String workPlanStatus;

    public WorkPlanDto(WorkPlan workPlan) {
        this.workPlanId = workPlan.getWorkPlanId();
        this.productId = workPlan.getProduct().getProductId();
        this.productName = workPlan.getProduct().getProductName(); // Product의 이름이 있다면 추가
        this.quantity = workPlan.getQuantity();
        this.start = workPlan.getStart();
        this.end = workPlan.getEnd();
        this.workPlanStatus = workPlan.getWorkPlanStatus().toString(); // Enum 값을 문자열로 변환
    }
}