package com.mesproject.entity;

import com.mesproject.constant.WorkOrdersStatus;
import jakarta.persistence.*;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class WorkOrders {
    @Id
    @GeneratedValue
    private Long workOrderId;

    //private Long workPlanId;

    private String workName;
    private LocalDateTime start;
    private LocalDateTime end;
    private Time duration;
    private LocalDateTime scheduledDate;

    @Enumerated(EnumType.STRING)
    private WorkOrdersStatus workOrdersStatus;

    private String worker;

}
