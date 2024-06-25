package com.mesproject.service;

import com.mesproject.dto.ProductionDataDto;
import com.mesproject.dto.WorkPlanDto;
import com.mesproject.entity.WorkOrders;
import com.mesproject.entity.WorkPlan;
import com.mesproject.repository.WorkOrdersRepository;
import com.mesproject.repository.WorkPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
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

//    public List<ProductionDataDto> getProductionDataByProduct() {
//        return workPlanRepository.aggregateProductionDataByProduct();
//    }

    public List<ProductionDataDto> getProductionDataByProduct() {
        List<ProductionDataDto> productionDataList = workPlanRepository.aggregateProductionDataByProduct();

        // 일자를 기준으로 데이터를 합치는 작업
        Map<LocalDate, ProductionDataDto> map = new LinkedHashMap<>();
        for (ProductionDataDto dto : productionDataList) {
            // LocalDateTime에서 LocalDate만 추출하여 키로 사용
            LocalDate endDate = dto.getEnd().toLocalDate();

            // 이미 같은 일자의 데이터가 있으면 수량을 더해줌
            if (map.containsKey(endDate)) {
                ProductionDataDto existingDto = map.get(endDate);
                existingDto.setTotalQuantity(existingDto.getTotalQuantity() + dto.getTotalQuantity());
            } else {
                // 같은 일자의 데이터가 없으면 맵에 추가
                map.put(endDate, dto);
            }
        }

        // 맵의 값을 리스트로 변환하여 반환
        return new ArrayList<>(map.values());
    }

}



