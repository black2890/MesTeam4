package com.mesproject.controller;
import com.mesproject.dto.WorkPlanDto;
import com.mesproject.entity.Product;
import com.mesproject.entity.WorkOrders;
import com.mesproject.entity.WorkPlan;
import com.mesproject.repository.ProductRepository;
import com.mesproject.repository.WorkPlanRepository;
import com.mesproject.service.WorkOrdersService;
import com.mesproject.service.WorkPlanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class WorkPlanController {

    private static final Logger logger = LoggerFactory.getLogger(WorkPlanController.class);

    @Autowired
    private WorkPlanService workPlanService;

    @Autowired
    private WorkPlanRepository workPlanRepository;

    @Autowired
    private WorkOrdersService workOrdersService;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/api/workplans")
    public ResponseEntity<List<WorkPlanDto>> getAllWorkPlansWithProduct() {
        List<WorkPlanDto> workPlanDTOs = workPlanService.getAllWorkPlansWithProduct();
        return ResponseEntity.ok(workPlanDTOs);
    }
}
