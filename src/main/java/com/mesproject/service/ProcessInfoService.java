package com.mesproject.service;

import com.mesproject.repository.ProcessRepository;
import com.mesproject.repository.WorkOrdersRepository;
import com.mesproject.repository.WorkPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProcessInfoService {

    private final ProcessRepository processRepository;
    private final WorkOrdersRepository workOrdersRepository;
    private final WorkPlanRepository workPlanRepository;

    public List<String> getProcessList(){

        return processRepository.findDistinctProcessTypesOrdered();
    }



    public List<Long> getOperationRateData(String processName, LocalDateTime searchDate) {
        Long totalDurationMinutes = workOrdersRepository.findTotalDurationByWorkNameAndEnd(processName, searchDate);

        if (totalDurationMinutes == null) {
            totalDurationMinutes = 0L;
        }

        // 24시간 = 1440분
        Long totalMinutesInDay = 1440L;

        return Arrays.asList(totalDurationMinutes, totalMinutesInDay - totalDurationMinutes);
    }


    public List<Long> getOutPutData(String processName, LocalDateTime searchDate){

        return workOrdersRepository.findQuantityByProcessNameAndSearchDate(processName, searchDate);
    }

    public List<Long> getDefectRateData(String processName, LocalDateTime searchDate){

        // 생산 계획량
        List<Long> planQuantities = workOrdersRepository.findQuantityByProcessNameAndSearchDate(processName, searchDate);
        // 실제 생산량
        List<Long> realQuantities = workOrdersRepository.findQuantityByInventoryStatusAndOrderPlanEnd(processName, searchDate);
        // 리스트 초기화
        List<Long> productionData = new ArrayList<>();

        // 실제 생산량과 생산 계획량 - 실제 생산량을 계산하여 리스트에 추가
        for (int i = 0; i < realQuantities.size(); i++) {
            Long realQuantity = realQuantities.get(i);
            Long planQuantity = planQuantities.get(i);

            Long calDefectRate = (planQuantity-realQuantity)*100/(planQuantity*100);

            productionData.add(calDefectRate);
        }

        return productionData;

    }

}
