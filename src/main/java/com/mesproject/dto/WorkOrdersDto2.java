package com.mesproject.dto;

import com.mesproject.constant.ProcessType;
import com.mesproject.constant.WorkOrdersStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Getter
@Setter
public class WorkOrdersDto2 {

    private Long workOrderId;
    private String productName;
    private ProcessType processType;
    private String worker;
    private LocalDateTime scheduledDate;
    private Duration duration;
    private LocalDateTime start;
    private LocalDateTime end;
    private WorkOrdersStatus workOrdersStatus;

}