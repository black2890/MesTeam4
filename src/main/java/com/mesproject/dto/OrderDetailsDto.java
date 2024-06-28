package com.mesproject.dto;

import com.mesproject.entity.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderDetailsDto {
    private Orders order;
    private List<MaterialOrders> materialOrders;
    private List<MaterialInOut> materialInOuts;
    private List<WorkOrders> workOrders;
    private List<WorkPlan> workPlans;
    private List<Inventory> inventories;
}
