package com.mesproject.entity;

import com.mesproject.constant.InventoryStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inventoryId;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="product_id")
    private Product product;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="work_plan_id")
    private WorkPlan workPlan;

    private Long quantity;

    @Enumerated(EnumType.STRING)
    private InventoryStatus inventoryStatus;


    private LocalDateTime storageDate;
    private LocalDateTime retrievalDate;

    public static Inventory createInventory(WorkPlan workPlan){

        Inventory inventory = new Inventory();
        inventory.setProduct(workPlan.getProduct());
        inventory.setWorkPlan(workPlan);
        inventory.setQuantity(workPlan.getQuantity());
        inventory.setInventoryStatus(InventoryStatus.STORAGECOMPLETED);

        return inventory;
    }
    public static Inventory updateInventory(WorkPlan workPlan, Long quantity){

        Inventory inventory = new Inventory();
        inventory.setProduct(workPlan.getProduct());
        inventory.setWorkPlan(workPlan);
        inventory.setQuantity(quantity);
        inventory.setInventoryStatus(InventoryStatus.RETRIEVALCOMPLETED);

        return inventory;
    }


}


