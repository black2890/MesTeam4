package com.mesproject.entity;

import com.mesproject.constant.ProcessType;
import com.mesproject.constant.WorkOrdersStatus;
import jakarta.persistence.*;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;



import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class WorkOrders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long workOrderId;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="work_plan_id")
    private WorkPlan workPlan;

    @Enumerated(EnumType.STRING)
    private ProcessType processType;
    private LocalDateTime start;
    private LocalDateTime end;
    private Duration duration;
    private LocalDateTime scheduledDate;

    @Enumerated(EnumType.STRING)
    private WorkOrdersStatus workOrdersStatus;

    private String worker;
    public static WorkOrders createWorkOrders(WorkPlan workPlan){

        WorkOrders workOrders = new WorkOrders();
    //    workOrders.setWorkPlanId(workPlan.getWorkPlanId());


        return workOrders;
    }


    public boolean isCompleted() {
        return workOrdersStatus == WorkOrdersStatus.COMPLETED;
    }
}
