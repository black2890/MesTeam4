package com.mesproject.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class BOM {
    @Id
    @GeneratedValue
    private Long bomId;
    //private Long productId;
    //private Long materialId;
}
