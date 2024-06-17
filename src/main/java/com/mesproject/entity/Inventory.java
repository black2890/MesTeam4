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
    // private Long productId;
    // private Long workPlanId;

    private Long quantity;

    @Enumerated(EnumType.STRING)
    private InventoryStatus inventoryStatus;

}
