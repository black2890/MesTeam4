package com.mesproject.controller;

import com.mesproject.constant.EquipmentStatus;
import com.mesproject.dto.EquipmentStatusUpdateRequest;
import com.mesproject.dto.ProcessInfoDto;
import com.mesproject.service.ProcessInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ProcessInfoController {

    private final ProcessInfoService processInfoService;

    @PostMapping("/api/createChart")
    public ResponseEntity<?> createChart(@RequestBody ProcessInfoDto request) {

        String processName = request.getProcessText();
        LocalDateTime searchDate = request.getSearchDate();

        List<Long> operationRateData = processInfoService.getOperationRateData(processName, searchDate);
        List<Long> outPutData = processInfoService.getOutPutData(processName, searchDate);
        List<Long> defectRateData = processInfoService.getDefectRateData(processName, searchDate);

        log.info("가동률", operationRateData);
        log.info("생산량", outPutData);
        log.info("불량률", defectRateData);

        Map<String, List<List<Long>>> response = new HashMap<>();
        response.put("data", Arrays.asList(operationRateData, outPutData, defectRateData));

        return ResponseEntity.ok(response);
    }
}
