package com.mesproject.entity;

import com.mesproject.constant.InventoryStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

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
    private String storageWorker;
    private String retrievalWorker;


    public static Inventory createInventory(WorkPlan workPlan){


        Random random = new Random();

        // 1%에서 3% 사이의 난수 생성
        int randomPercentage = 1 + random.nextInt(3);
        double defectRate = 1-randomPercentage*0.01;

        Inventory inventory = new Inventory();
        inventory.setProduct(workPlan.getProduct());
        inventory.setWorkPlan(workPlan);
        inventory.setQuantity((long) Math.floor(workPlan.getQuantity()*defectRate));
        inventory.setInventoryStatus(InventoryStatus.PENDINGSTORAGE);

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


