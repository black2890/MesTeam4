package com.mesproject.dto;

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
public class WorkOrdersDto {

    private Long workOrderId;
    private String productName;
    private String workName;
    private String worker;
    private LocalDateTime scheduledDate;
    private Time duration;
    private LocalDateTime start;
    private LocalDateTime end;
    private WorkOrdersStatus workOrdersStatus;

}
