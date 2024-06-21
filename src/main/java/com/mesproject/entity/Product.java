package com.mesproject.entity;

import com.mesproject.constant.productType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.util.List;

@Entity
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue
    private Long productId;
    private Long routingId;
    private String productName;

    @Enumerated(EnumType.STRING)
    private productType productType;

    private Long minProduction;
    private Long maxProduction;
    private Time leadTime;

}
