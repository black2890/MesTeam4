package com.mesproject.entity;

import com.mesproject.constant.MaterialInOutStatus;
import com.mesproject.constant.MaterialOrdersStatus;
import com.mesproject.dto.MaterialOrderDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class MaterialInOut {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inoutId;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="material_order_id")
    private MaterialOrders materialOrders;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="product_id")
    private Product product;

    private Long quantity;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="work_order_id")
    private WorkOrders workOrders;

    private LocalDateTime expirationDate;

    @Enumerated(EnumType.STRING)
    private MaterialInOutStatus materialInOutStatus;

    private LocalDateTime storageDate;
    private LocalDateTime retrievalDate;
    private String storageWorker;
    private String retrievalWorker;

    public static MaterialInOut createMaterialInOut(MaterialOrders materialOrders){

        MaterialInOut materialInOut = new MaterialInOut();
        materialInOut.setMaterialOrders(materialOrders);
        materialInOut.setProduct(materialOrders.getProduct());
        materialInOut.setQuantity(materialOrders.getQuantity());
        materialInOut.setMaterialInOutStatus(MaterialInOutStatus.PENDINGSTORAGE);


        return materialInOut;
    }

    public static MaterialInOut updateMaterialInOut(MaterialInOut materialInOut, Long quantity,LocalDateTime start, String worker){

        MaterialInOut newMaterialInOut = new MaterialInOut();
        newMaterialInOut.setMaterialOrders(materialInOut.getMaterialOrders());
        newMaterialInOut.setProduct(materialInOut.getProduct());
        newMaterialInOut.setQuantity(quantity);
        newMaterialInOut.setWorkOrders(materialInOut.getWorkOrders());
        newMaterialInOut.setExpirationDate(materialInOut.getExpirationDate());
        newMaterialInOut.setMaterialInOutStatus(MaterialInOutStatus.RETRIEVAL);
        newMaterialInOut.setStorageDate(materialInOut.getStorageDate());
        newMaterialInOut.setRetrievalDate(start);
        newMaterialInOut.setStorageWorker(materialInOut.getStorageWorker());
        newMaterialInOut.setRetrievalWorker(worker);

        return newMaterialInOut;
    }
}
