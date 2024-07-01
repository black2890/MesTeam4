package com.mesproject.dto;

import com.mesproject.constant.vendorType;
import com.mesproject.entity.Orders;
import com.mesproject.entity.Vendor;
import com.mesproject.entity.WorkOrders;
import com.mesproject.entity.WorkPlan;
import com.mesproject.service.OrderService;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VendorDto {

    private Long vendorId;
    private String vendorName;
    private String contactInfo;

    private vendorType vendorType;

    private boolean deleted;
    private LocalDate recentOrderDate;


    public static VendorDto createVendorDto(Vendor vendor, Optional<Orders> tempOrders) {
        LocalDate tempRecentOrderDate;
        Orders orders;

        if (tempOrders.isPresent()) {
            orders = tempOrders.get();
            tempRecentOrderDate = orders.getRegDate();

        } else {
          tempRecentOrderDate = null;
        }


        return new VendorDto(
                vendor.getVendorId(),
                vendor.getVendorName(),
                vendor.getContactInfo(),
                vendor.getVendorType(),
                vendor.isDeleted(),
                tempRecentOrderDate


                );
    }
}


