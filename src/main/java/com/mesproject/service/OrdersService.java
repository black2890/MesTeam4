package com.mesproject.service;

import com.mesproject.dto.DataTableDto;
import com.mesproject.dto.OrdersDto;
import com.mesproject.entity.Orders;
import com.mesproject.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrdersService {

    private final OrdersRepository ordersRepository;

    public DataTableDto getOrdersData(MultiValueMap<String, String> formData) {
        int draw = Integer.parseInt(formData.get("draw").get(0));
        int start = Integer.parseInt(formData.get("start").get(0));
        int length = Integer.parseInt(formData.get("length").get(0));


        // 페이지 설정
        Pageable pageable = PageRequest.of(start / length, length);

        // 검색 조건 가져오기
        String productName = formData.getFirst("columns[1][search][value]");
        String vendorName = formData.getFirst("columns[3][search][value]");

        // 페이지네이션을 이용한 데이터 조회
        Page<OrdersDto> ordersPage;

        if (StringUtils.isEmpty(productName) && StringUtils.isEmpty(vendorName)) {
            // 검색 조건이 없는 경우 전체 데이터 조회
            ordersPage = ordersRepository.findOrders(pageable);
        } else {
            // 검색 조건이 있는 경우 해당 조건으로 데이터 조회
            ordersPage = ordersRepository.findOrdersByProductOrVendor(
                    StringUtils.isEmpty(productName) ? null : productName,
                    StringUtils.isEmpty(vendorName) ? null : vendorName,
                    pageable
            );
        }

        // DataTableDto에 결과 저장
        DataTableDto dto = new DataTableDto();
        dto.setDraw(draw);
        dto.setRecordsFiltered((int) ordersPage.getTotalElements());
        dto.setRecordsTotal((int) ordersRepository.count());
        dto.setData(ordersPage.getContent());

        return dto;
    }
}