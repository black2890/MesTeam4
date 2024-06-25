package com.mesproject.controller;
import com.mesproject.dto.ProductionDataDto;
import com.mesproject.dto.WorkPlanDto;
import com.mesproject.entity.WorkPlan;
import com.mesproject.repository.ProductRepository;
import com.mesproject.repository.WorkPlanRepository;
import com.mesproject.service.WorkOrdersService;
import com.mesproject.service.WorkPlanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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

    @GetMapping("/api/production-data")
    public ResponseEntity<List<ProductionDataDto>> getProductionDataByProduct() {
        List<ProductionDataDto> productionData = workPlanService.getProductionDataByProduct();
        return ResponseEntity.ok().body(productionData);
    }
}
