package com.mesproject.dto;

import com.mesproject.constant.ProcessType;
import com.mesproject.constant.WorkOrdersStatus;
import com.mesproject.entity.WorkOrders;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.jdbc.Work;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkOrdersDto {
    private Long workOrderId;
    private String productName;
    private ProcessType processType;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long duration;
    private LocalDateTime scheduledDate;
    private WorkOrdersStatus workOrdersStatus;
    private String worker;

    public static WorkOrdersDto createWorkOrdersDto(WorkOrders workOrders) {

        return new WorkOrdersDto(workOrders.getWorkOrderId(),
                workOrders.getWorkPlan().getProduct().getProductName(),
                workOrders.getProcessType(),
                workOrders.getStart(),
                workOrders.getEnd(),
                workOrders.getDuration().toHours(),
                workOrders.getScheduledDate(),
                workOrders.getWorkOrdersStatus(),
                workOrders.getWorker()
                );
    }
}
