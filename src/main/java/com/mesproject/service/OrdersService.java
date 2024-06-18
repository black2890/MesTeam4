package com.mesproject.service;

import com.mesproject.dto.DataTableDto;
import com.mesproject.entity.Orders;
import com.mesproject.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.Order;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrdersService {

    private final OrdersRepository ordersRepository;

    public DataTableDto getPagingData(DataTableDto dto, @RequestBody MultiValueMap<String,String> formData){
        int draw = Integer.parseInt(formData.get("draw").get(0));
        int start = Integer.parseInt(formData.get("start").get(0));
        int length = Integer.parseInt(formData.get("length").get(0));

        int total = (int) ordersRepository.count();
        List<Orders> data = ordersRepository.findData(start, length);

        dto.setDraw(draw);
        dto.setRecordsFiltered(total);
        dto.setRecordsTotal(total);
        dto.setData(data);

        return dto;
    }


    public List<Orders> getAllOrders(){
        return ordersRepository.findAll();
    }
}