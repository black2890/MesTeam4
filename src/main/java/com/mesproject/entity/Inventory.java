package com.mesproject.entity;

import com.mesproject.constant.InventoryStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Inventory {
    @Id
    @GeneratedValue
    private Long inventoryId;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="product_id")
    private Product product;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="work_plan_id")
    private WorkPlan workPlan;

    private Long quantity;

    @Enumerated(EnumType.STRING)
    private InventoryStatus inventoryStatus;

}
