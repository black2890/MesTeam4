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

    @PostMapping("/ordersData")
    public DataTableDto getOrders(@RequestBody MultiValueMap<String, String> formData) {

        DataTableDto dto = new DataTableDto();
        return ordersService.getPagingData(dto, formData);
    }
}
