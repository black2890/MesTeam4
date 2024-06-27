package com.mesproject.controller;

import com.mesproject.dto.DataTableDto;
import com.mesproject.service.MaterialOrdersService;
import com.mesproject.service.WorkOrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class WorkOrdersController {

    private final WorkOrdersService workOrdersService;

//    @PostMapping("/api/workOrdersData") // Post api로 작업 지시 현황 데이터 반환 컨트롤러
//    public DataTableDto getWorkOrders(@RequestBody MultiValueMap<String, String> formData) {
//        return workOrdersService.getWorkOrdersData(formData);
//    }
}

