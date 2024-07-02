package com.mesproject.controller;

import com.mesproject.constant.EquipmentStatus;
import com.mesproject.dto.DataTableDto;
import com.mesproject.dto.EquipmentStatusUpdateRequest;
import com.mesproject.service.EquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class EquipmentController {

    private final EquipmentService equipmentService;

    @PostMapping("/api/equipmentData") // Post api로 설비 현황 테이블 데이터 반환 컨트롤러
    public DataTableDto getEquipment(@RequestBody MultiValueMap<String, String> formData) {
        return equipmentService.getEquipmentData(formData);
    }

    @PostMapping("/api/updateEquipmentStatus")
    public ResponseEntity<?> updateEquipmentStatus(@RequestBody EquipmentStatusUpdateRequest request) {
        // request에서 orderIds와 status를 가져와서 처리
        List<Long> equipmentIds = request.getEquipmentIds();
        List<EquipmentStatus> equipmentStatuses = request.getEquipmentStatus();

        // 여기서 orderService를 사용하여 주문 상태를 업데이트
        equipmentService.updateEquipmentStatus(equipmentIds, equipmentStatuses);

        return ResponseEntity.ok().build();
    }
}
