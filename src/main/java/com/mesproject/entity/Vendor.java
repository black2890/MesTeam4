package com.mesproject.entity;

import com.mesproject.constant.vendorType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Setter
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vendorId;
    private String vendorName;
    private String contactInfo;

    @Enumerated(EnumType.STRING)
    private vendorType vendorType;

    @ColumnDefault("false")
    private boolean deleted = false;
}
