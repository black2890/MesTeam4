package com.mesproject.service;

import com.mesproject.entity.WorkOrders;
import com.mesproject.repository.ProcessRepository;
import com.mesproject.repository.WorkOrdersRepository;
import com.mesproject.repository.WorkPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

        String searchDateString = searchDate.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        // 생산 계획량
        workOrdersRepository.findQuantityByProcessNameAndSearchDate(processName, searchDate);
        // 실제 생산량
        workOrdersRepository.findQuantityByInventoryStatusAndOrderPlanEnd(processName, searchDate);

    }

}
