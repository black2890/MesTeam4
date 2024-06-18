package com.mesproject.entity;

import com.mesproject.constant.MaterialOrdersStatus;
import com.mesproject.constant.OrdersStatus;
import com.mesproject.constant.vendorType;
import com.mesproject.dto.MaterialOrderDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class MaterialOrders {

    @Id
    @GeneratedValue
    private Long materialOrderId;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="product_id")
    private Product product;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="vendor_id")
    private Vendor vendor;

    private LocalDateTime materialOrderDate;
    private LocalDateTime deliveryDate;
    private Long quantity;

    @Enumerated(EnumType.STRING)
    private MaterialOrdersStatus materialOrdersStatus;

    public static MaterialOrders createMaterialOrders(MaterialOrderDto materialOrderDto, Product product){

        MaterialOrders materialOrders = new MaterialOrders();
        materialOrders.setProduct(product);
        materialOrders.setQuantity(materialOrderDto.getQuantity());
        materialOrders.setMaterialOrdersStatus(MaterialOrdersStatus.PENDINGSTORAGE);

        return materialOrders;
    }

}
