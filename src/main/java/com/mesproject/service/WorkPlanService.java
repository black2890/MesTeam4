package com.mesproject.service;

import com.mesproject.constant.WorkOrdersStatus;
import com.mesproject.dto.*;
import com.mesproject.entity.Product;
import com.mesproject.entity.WorkOrders;
import com.mesproject.entity.WorkPlan;
import com.mesproject.repository.WorkOrdersRepository;
import com.mesproject.repository.WorkPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
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

    public List<WorkOrderDto> getWorkOrdersByWorkPlanId(Long workPlanId) {
        WorkPlan workPlan = workPlanRepository.findById(workPlanId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "WorkPlan not found with id " + workPlanId));

        return workPlan.getWorkOrders().stream()
                .map(order -> new WorkOrderDto(order))
                .collect(Collectors.toList());
    }

    public List<WorkPlanProgressDto> getWorkPlanProgresses() {
        List<Long> inProgressWorkPlanIds = workOrdersRepository.findWorkPlanIdsByWorkOrdersStatusInProgress();
        List<WorkPlanProgressDto> workPlanProgresses = new ArrayList<>();

        for (Long workPlanId : inProgressWorkPlanIds) {
            List<WorkOrders> workOrders = workOrdersRepository.findByWorkPlan_WorkPlanId(workPlanId);
            if (!workOrders.isEmpty()) {
                WorkOrders representativeWorkOrder = workOrders.get(0);
                WorkPlan workPlan = representativeWorkOrder.getWorkPlan();
                Product product = workPlan.getProduct();

                // 계획 단위 DTO 생성
                WorkPlanProgressDto progressDto = new WorkPlanProgressDto();
                progressDto.setProductName(product.getProductName());

                // 제품에 해당하는 공정 타입과 진행도를 담을 DTO 생성
                List<ProcessProgressDto> processProgressList = new ArrayList<>();
                // 각 작업 지시를 반복문을 통해 순환하며 ProcessType 공정 타입 지정
                for (WorkOrders workOrder : workOrders) {
                    ProcessProgressDto processProgressDto = new ProcessProgressDto();
                    processProgressDto.setProcessType(workOrder.getProcessType());

                    // 각 작업 지시의 상태에 따라 0~ 100% 진행도를 부여 및 계산하여 DTO에 Progress 진행도 저장.
                    if (workOrder.getWorkOrdersStatus() == WorkOrdersStatus.PENDING) {
                        processProgressDto.setProgress(0.0);
                    } else if (workOrder.getWorkOrdersStatus() == WorkOrdersStatus.COMPLETED) {
                        processProgressDto.setProgress(100.0);
                    } else {
                        Duration totalDuration = Duration.between(workOrder.getStart(), workOrder.getEnd());
                        Duration elapsedDuration = Duration.between(workOrder.getStart(), LocalDateTime.now());
                        double progress = (double) elapsedDuration.toMinutes() / totalDuration.toMinutes() * 100;
                        processProgressDto.setProgress(progress);
                    }


                    processProgressList.add(processProgressDto);
                }

                progressDto.setProcessProgressList(processProgressList);
                workPlanProgresses.add(progressDto);
            }
        }

        return workPlanProgresses;
    }


}



