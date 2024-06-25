package com.mesproject.controller;

import com.mesproject.constant.EquipmentStatus;
import com.mesproject.dto.EquipmentStatusUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ProcessInfoController {

//    @PostMapping("/api/createHalfDoughnutChart")
//    public ResponseEntity<?> createHalfDoughnutChart(@RequestBody EquipmentStatusUpdateRequest request) {
//        // request에서 orderIds와 status를 가져와서 처리
//        List<Long> equipmentIds = request.getEquipmentIds();
//        List<EquipmentStatus> equipmentStatuses = request.getEquipmentStatus();
//
//        // 여기서 orderService를 사용하여 주문 상태를 업데이트
//        equipmentService.updateEquipmentStatus(equipmentIds, equipmentStatuses);
//
//        return ResponseEntity.ok().build();
//    }


}
