package com.mesproject.entity;

import com.mesproject.constant.WorkOrdersStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class WorkPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long workPlanId;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name="product_id")
    private Product product;

    private Long quantity;
    private LocalDateTime start;
    private LocalDateTime end;

    @OneToMany(mappedBy = "workPlan",
            cascade = CascadeType.ALL
            ,orphanRemoval = true,
            fetch=FetchType.LAZY)
    private List<WorkOrders> workOrders = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private WorkOrdersStatus workPlanStatus;



    public static WorkPlan createWorkPlan(Product product, Long quantity){

        WorkPlan workPlan = new WorkPlan();
        workPlan.setProduct(product);
        workPlan.setQuantity(quantity);
        workPlan.setWorkPlanStatus(WorkOrdersStatus.PENDING);

        return workPlan;
    }
}
