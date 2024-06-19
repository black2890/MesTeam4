package com.mesproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.aspectj.weaver.ast.Or;
import org.hibernate.jdbc.Work;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class OrdersPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ordersPlanId;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="orders_id")
    private Orders orders;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="work_plan_id")
    private WorkPlan workPlan;



    public static OrdersPlan createOrdersPlan(WorkPlan workPlan){

        OrdersPlan ordersPlan = new OrdersPlan();
        ordersPlan.setWorkPlan(workPlan);

        return ordersPlan;
    }

}
