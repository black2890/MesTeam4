package com.mesproject.entity;

import com.mesproject.constant.InventoryStatus;
import com.mesproject.constant.MaterialOrdersStatus;
import com.mesproject.dto.MaterialOrderDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="work_plan_id")
    private WorkPlan workPlan;

    private Long quantity;

    @Enumerated(EnumType.STRING)
    private InventoryStatus inventoryStatus;

    public static Inventory createInventory(WorkPlan workPlan){

        Inventory inventory = new Inventory();
        inventory.setProduct(workPlan.getProduct());
        inventory.setWorkPlan(workPlan);
        inventory.setQuantity(workPlan.getQuantity());
        inventory.setInventoryStatus(InventoryStatus.STORAGECOMPLETED);

        return inventory;
    }

}
