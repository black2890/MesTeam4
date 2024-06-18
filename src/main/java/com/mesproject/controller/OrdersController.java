package com.mesproject.controller;

import com.mesproject.dto.DataTableDto;
import com.mesproject.service.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
public class OrdersController {

    private final OrdersService ordersService;

    @PostMapping("/api/ordersData") // Post api로 수주 테이블 데이터 반환 컨트롤러
    public DataTableDto getOrders(@RequestBody MultiValueMap<String, String> formData) {
        return ordersService.getOrdersData(formData);
    }
}
