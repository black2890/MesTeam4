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

import java.text.DecimalFormat;
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

    // 작업 계획 아이디에 해당하는 작업 지시 가져오는 메소드
    public List<WorkOrderDto> getWorkOrdersByWorkPlanId(Long workPlanId) {

        WorkPlan workPlan = workPlanRepository.findById(workPlanId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "WorkPlan not found with id " + workPlanId));

        return workPlan.getWorkOrders().stream()
                .map(order -> new WorkOrderDto(order))
                .collect(Collectors.toList());
    }

    // 작업 계획 진행률 가져오는 메소드
    public List<WorkPlanProgressDto> getWorkPlanProgresses() {

        // 작업 지시에서 진행중인 작업 계획 아이디 리스트 생성 및 선언
        List<Long> inProgressWorkPlanIds = workOrdersRepository.findWorkPlanIdsByWorkOrdersStatusInProgress();

        // 작업 계획 진행률을 담는 dto 리스트 생성
        List<WorkPlanProgressDto> workPlanProgresses = new ArrayList<>();

        // 소수점 설정 포멧
        DecimalFormat df = new DecimalFormat("#.#");

        // 작업 지시에서 진행중인 작업 계획 아이디 리스트에서
        // 아이디 각각에 대한 작업 지시를 담는 리스트 생성 및 선언
        for (Long workPlanId : inProgressWorkPlanIds) {
            List<WorkOrders> workOrders = workOrdersRepository.findByWorkPlan_WorkPlanId(workPlanId);

            // 작업 지시 리스트가 비어 있지 않다면 ( = 현재 진행중인 작업 계획이 있다는 것)
            if (!workOrders.isEmpty()) {
                WorkOrders representativeWorkOrder = workOrders.get(0);
                WorkPlan workPlan = representativeWorkOrder.getWorkPlan();
                Product product = workPlan.getProduct();

                // 작업 계획의 각 작업 제품명과 공정 타입 및 진행 정보를 담는 DTO 생성
                WorkPlanProgressDto progressDto = new WorkPlanProgressDto();

                // 1. 작업 계획 DTO에서 제품명 설정
                progressDto.setProductName(product.getProductName());

                // 작업 계획 DTO에서 공정 타입 및 진행도를 담는 리스트 파라미터 생성
                List<ProcessProgressDto> processProgressList = new ArrayList<>();

                // 각 작업 지시를 반복문을 통해 순환하여 ProcessType 공정 타입 지정
                for (WorkOrders workOrder : workOrders) {

                    //공정 타입 및 진행도를 담는 DTO 생성
                    ProcessProgressDto processProgressDto = new ProcessProgressDto();
                    //순서대로 DB에서 가져온 공정 타입을 파라미터에 할당
                    processProgressDto.setProcessType(workOrder.getProcessType());

                    // 각 작업 지시의 상태에 따라 0~ 100% 진행도를 부여 및 계산하여 DTO에 Progress 진행도 저장.

                    // 1. 작업 지시의 상태가 보류 인 경우 0% 부여
                    if (workOrder.getWorkOrdersStatus() == WorkOrdersStatus.PENDING) {
                        processProgressDto.setProgress(0.0);
                    // 2. 작업 지시의 상태가 완료 인 경우 100% 부여
                    } else if (workOrder.getWorkOrdersStatus() == WorkOrdersStatus.COMPLETED) {
                        processProgressDto.setProgress(100.0);
                    // 3. 외의 경우 진행률 계산 (공정 시작 시간 기준으로, (지금까지 걸린 시간 / 예상 소요 시간) 으로 진행률 표시
                    } else {
                        Duration totalDuration = Duration.between(workOrder.getStart(), workOrder.getEnd());
                        Duration elapsedDuration = Duration.between(workOrder.getStart(), LocalDateTime.now());
                        double progress = (double) elapsedDuration.toMinutes() / totalDuration.toMinutes() * 100;
                        if(progress>=100)progress=99.9;
                        String formattedProgress = df.format(progress);
                        processProgressDto.setProgress(Double.parseDouble(formattedProgress));
                    }

                    // 작업 지시를 나타내는 작업 계획에서 각 공정 종류 및 진행률 할당
                    processProgressList.add(processProgressDto);
                }

                // 2. 작업 계획이 포함하는 공정 타입 및 진행률 리스트 할당
                progressDto.setProcessProgressList(processProgressList);

                //각 제품별로 작업 계획 할당
                workPlanProgresses.add(progressDto);
            }
        }

        // 작업 계획에 해당하는 제품명 및 제품명에 해당하는 각 공정별 타입 및 진행률 리스트 형식의 각 제품별 작업 계획 List 반환
        return workPlanProgresses;
    }
}



