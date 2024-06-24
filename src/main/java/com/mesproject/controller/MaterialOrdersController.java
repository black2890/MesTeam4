package com.mesproject.controller;

import com.mesproject.dto.DataTableDto;
import com.mesproject.service.MaterialOrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MaterialOrdersController {

    private final MaterialOrdersService materialOrdersService;

    @PostMapping("/api/materialOrdersData") // Post api로 자재 주문 현황 데이터 반환 컨트롤러
    public DataTableDto getMaterialOrders(@RequestBody MultiValueMap<String, String> formData) {
        return materialOrdersService.getMaterialOrdersData(formData);
    }
}
