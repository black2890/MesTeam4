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

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="material_order_id")
    private MaterialOrders materialOrders;

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
        materialInOut.setMaterialInOutStatus(MaterialInOutStatus.PENDINGSTORAGE);


        return materialInOut;
    }
}
