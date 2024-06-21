package com.mesproject.service;

import com.mesproject.dto.DataTableDto;
import com.mesproject.dto.OrdersDto;
import com.mesproject.dto.WorkOrdersDto;
import com.mesproject.entity.Orders;
import com.mesproject.entity.WorkOrders;
import com.mesproject.repository.WorkOrdersRepository;
import lombok.RequiredArgsConstructor;
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
public class WorkOrdersService {

    private final WorkOrdersRepository workOrdersRepository;

    // 페이지 및
    public DataTableDto getWorkOrdersData(MultiValueMap<String, String> formData) {
        int draw = Integer.parseInt(formData.get("draw").get(0));
        int start = Integer.parseInt(formData.get("start").get(0));
        int length = Integer.parseInt(formData.get("length").get(0));

        System.out.println(start +"입니다.");
        System.out.println(length +"입니다.");

        // 페이지 설정
        Pageable pageable = PageRequest.of(start / length, length);

        // 검색 조건 가져오기
        String searchType = formData.getFirst("searchType");
        String searchValue = formData.getFirst("searchValue");

        System.out.println(searchType +"입니다.");
        System.out.println(searchValue +"입니다.");

        // 페이지네이션을 이용한 데이터 조회
        Page<WorkOrdersDto> workOrdersPage;

        if (searchType.equals("검색 조건") || StringUtils.isEmpty(searchValue)) {
            // 검색 조건이 없는 경우 전체 데이터 조회
            workOrdersPage = workOrdersRepository.findWorkOrders(pageable);
        } else {
            // 검색 조건이 있는 경우 해당 조건으로 데이터 조회
            workOrdersPage = workOrdersRepository.findWorkOrdersBySearchOption(
                    searchType,
                    searchValue,
                    pageable
            );
        }

        System.out.println(workOrdersPage.getContent());

        // DataTableDto에 결과 저장
        DataTableDto dto = new DataTableDto();
        dto.setDraw(draw);
        dto.setRecordsFiltered((int) workOrdersPage.getTotalElements());
        dto.setRecordsTotal((int) workOrdersRepository.count());
        dto.setData(workOrdersPage.getContent());

        return dto;
    }

}