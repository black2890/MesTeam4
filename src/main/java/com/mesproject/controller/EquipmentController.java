
// 하드 코딩으로 대체

//package com.mesproject.controller;
//
//import com.mesproject.dto.DataTableDto;
//import com.mesproject.service.EquipmentService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//@RequiredArgsConstructor
//@RestController
//public class EquipmentController {
//
//    private final EquipmentService equipmentService;
//
//    @PostMapping("/api/equipmentData") // Post api로 설비 현황 테이블 데이터 반환 컨트롤러
//    public DataTableDto getEquipment(@RequestBody MultiValueMap<String, String> formData) {
//        return equipmentService.getEquipmentsData(formData);
//    }
//}
