package com.mesproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.aspectj.weaver.ast.Or;
import org.springframework.boot.autoconfigure.web.WebProperties;

@Entity
@Getter
@Setter
public class OrdersMaterials {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="orders_materials_id")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="orders_id")
    private Orders orders;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="material_orders_id")
    private MaterialOrders materialOrders;

    public static OrdersMaterials createOrdersMaterials(MaterialOrders materialOrders){

        OrdersMaterials ordersMaterials = new OrdersMaterials();
        ordersMaterials.setMaterialOrders(materialOrders);

        return ordersMaterials;
    }
}
