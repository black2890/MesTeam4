package com.mesproject.dto;

import com.mesproject.constant.ProcessType;
import com.mesproject.constant.WorkOrdersStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Setter
public class WorkOrdersDto {
    private Long workOrderId;
    private ProcessType processType;
    private LocalDateTime start;
    private LocalDateTime end;
    private Duration duration;
    private LocalDateTime scheduledDate;
    private WorkOrdersStatus workOrdersStatus;
    private String worker;

}
