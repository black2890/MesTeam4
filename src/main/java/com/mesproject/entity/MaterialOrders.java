package com.mesproject.entity;

import com.mesproject.constant.MaterialOrdersStatus;
import com.mesproject.constant.OrdersStatus;
import com.mesproject.constant.vendorType;
import com.mesproject.dto.MaterialOrderDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class MaterialOrders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long materialOrderId;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="product_id")
    private Product product;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="vendor_id")
    private Vendor vendor;

    private LocalDateTime materialOrderDate;
   // private LocalDateTime deliveryDate;

    private LocalDate deliveryDate;
    private Long quantity;

    @Enumerated(EnumType.STRING)
    private MaterialOrdersStatus materialOrdersStatus;

    public static MaterialOrders createMaterialOrders(MaterialOrderDto materialOrderDto, Product product){

        MaterialOrders materialOrders = new MaterialOrders();
        materialOrders.setProduct(product);
        materialOrders.setQuantity(materialOrderDto.getQuantity());
        materialOrders.setMaterialOrdersStatus(MaterialOrdersStatus.PENDINGSTORAGE);
        LocalDateTime materialOrderDate = materialOrderDto.getMaterialOrderDate();
        materialOrders.setMaterialOrderDate(materialOrderDate);
        materialOrders.setDeliveryDate(MaterialDeliveryDateCalculator.calculateDeliveryDate(materialOrderDate,product.getProductId()));
//        materialOrders.setDeliveryDate(materialOrderDto.getDeliveryDate());
//        if(product.getProductId()==5||product.getProductId()==6||product.getProductId()==7){
//            materialOrders.setMaterialOrderDate(materialOrderDto.getDeliveryDate().minusDays(2));
//        } else if (product.getProductId() == 8 || product.getProductId() ==9 ||product.getProductId()== 10||product.getProductId() ==12) {
//            materialOrders.setMaterialOrderDate(materialOrderDto.getDeliveryDate().minusDays(3));
//        }else if(product.getProductId()==11){
//
//        }


        return materialOrders;
    }

    public class MaterialDeliveryDateCalculator {
        private static final Set<LocalDate> HOLIDAYS = new HashSet<>(Arrays.asList(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 2, 9),
                LocalDate.of(2024, 3, 1)

        ));
        public static LocalDate calculateDeliveryDate(LocalDateTime orderDateTime, Long productId) {
            LocalDate orderDate = orderDateTime.toLocalDate();
            LocalTime orderTime = orderDateTime.toLocalTime();

            //즙이면, 2일/3일
            //스틱이면 3일/4일
            int leadTime=0;
            if(productId==5||productId==6||productId==7){
            leadTime = orderTime.isBefore(LocalTime.NOON) ? 2 : 3;
            }else if(productId==8||productId==9||productId==10){
                leadTime = orderTime.isBefore(LocalTime.of(15, 0)) ? 3 : 4;
            }else if(productId==11){
                leadTime = 7;
            }else if(productId==12){
                leadTime = 3;
            }

            LocalDate estimatedDeliveryDate = orderDate.plusDays(leadTime);

            while (isWeekendOrHoliday(estimatedDeliveryDate)) {
                estimatedDeliveryDate = estimatedDeliveryDate.plusDays(1);
            }
            return estimatedDeliveryDate;
        }
        private static boolean isWeekendOrHoliday(LocalDate date) {
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY || HOLIDAYS.contains(date);
        }
    }

}
