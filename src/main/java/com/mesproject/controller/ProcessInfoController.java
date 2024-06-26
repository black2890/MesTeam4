package com.mesproject.controller;

import com.mesproject.constant.EquipmentStatus;
import com.mesproject.dto.EquipmentStatusUpdateRequest;
import com.mesproject.dto.ProcessInfoDto;
import com.mesproject.service.ProcessInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ProcessInfoController {

    private final ProcessInfoService processInfoService;

    @PostMapping("/api/createHalfDoughnutChart")
    public ResponseEntity<?> createHalfDoughnutChart(@RequestBody ProcessInfoDto request) {

        String processName = request.getProcessText();
        LocalDateTime searchDate = request.getSearchDate();

        List<Long> operationRateData = processInfoService.getOperationRateData(processName, searchDate);
        List<Long> outPutData = processInfoService.getOutPutData(processName, searchDate);
        List<Long> defectRateData = processInfoService.getDefectRateData(processName, searchDate);

        return ResponseEntity.ok().build();
    }
}
