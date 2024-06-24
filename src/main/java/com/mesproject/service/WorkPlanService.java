package com.mesproject.service;

import com.mesproject.dto.WorkPlanDto;
import com.mesproject.entity.WorkOrders;
import com.mesproject.entity.WorkPlan;
import com.mesproject.repository.WorkOrdersRepository;
import com.mesproject.repository.WorkPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WorkPlanService {

    @Autowired
    private WorkPlanRepository workPlanRepository;

    @Autowired
    private WorkOrdersRepository workOrdersRepository;

    public List<WorkPlanDto> getAllWorkPlansWithProduct() {
        List<WorkPlan> workPlans = workPlanRepository.findAllWithProduct();
        return workPlans.stream()
                .map(workPlan -> {
                    WorkPlanDto dto = new WorkPlanDto(workPlan);
                    dto.setWorkPlanId(workPlan.getWorkPlanId());
                    dto.setQuantity(workPlan.getQuantity());
                    dto.setStart(workPlan.getStart());
                    dto.setEnd(workPlan.getEnd());
                    dto.setWorkPlanStatus(workPlan.getWorkPlanStatus().toString());
                    // Product 정보를 DTO에 매핑
                    if (workPlan.getProduct() != null) {
                        dto.setProductId(workPlan.getProduct().getProductId());
                        dto.setProductName(workPlan.getProduct().getProductName());
                    } else {
                        dto.setProductId(null);
                        dto.setProductName("Undefined Product");
                    }
                    return dto;
                })
                .collect(Collectors.toList());

    }

    public Map<String, Map<LocalDateTime, Long>> getProductionDataByEndDate() {
        // Fetch all work plans within a certain date range (e.g., last 30 days)
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(30); // Adjust as needed

        List<WorkPlan> workPlans = workPlanRepository.findByEndBetweenOrderByEndAsc(startDate, endDate);

        // Group work plans by product and aggregate quantities by end date
        Map<String, Map<LocalDateTime, Long>> productionData = workPlans.stream()
                .collect(Collectors.groupingBy(
                        workPlan -> workPlan.getProduct().getProductName(),
                        Collectors.groupingBy(WorkPlan::getEnd,
                                Collectors.summingLong(WorkPlan::getQuantity)
                        )
                ));

        return productionData;
    }
}

