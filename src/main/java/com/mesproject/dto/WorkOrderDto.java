package com.mesproject.dto;

import com.mesproject.entity.WorkOrders;
import lombok.Getter;
import lombok.Setter;
import java.time.Duration;

import java.time.LocalDateTime;

@Getter
@Setter
public class WorkOrderDto {
    private Long workOrderId;
    private String processType;
    private LocalDateTime start;
    private LocalDateTime end;
    private Duration duration;
    private LocalDateTime scheduledDate;
    private String workOrdersStatus;
    private String worker;

    public WorkOrderDto(WorkOrders workOrder) {
        this.workOrderId = workOrder.getWorkOrderId();
        this.processType = workOrder.getProcessType().name();
        this.start = workOrder.getStart();
        this.end = workOrder.getEnd();
        this.duration = workOrder.getDuration();
        this.scheduledDate = workOrder.getScheduledDate();
        this.workOrdersStatus = workOrder.getWorkOrdersStatus().name();
        this.worker = workOrder.getWorker();
    }
}
