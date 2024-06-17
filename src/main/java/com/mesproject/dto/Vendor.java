package com.mesproject.dto;

import com.mesproject.constant.vendorType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class Vendor {

    private Long vendorId;
    private String vendorName;
    private String contactInfo;

    @Enumerated(EnumType.STRING)
    private vendorType vendorType;
}
