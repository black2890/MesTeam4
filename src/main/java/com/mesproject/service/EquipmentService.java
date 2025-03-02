package com.mesproject.service;

import com.mesproject.constant.EquipmentStatus;
import com.mesproject.constant.OrdersStatus;
import com.mesproject.dto.DataTableDto;
import com.mesproject.dto.EquipmentDto;
import com.mesproject.dto.OrdersDto;
import com.mesproject.entity.Equipment;
import com.mesproject.entity.Orders;
import com.mesproject.repository.EquipmentRepository;
import com.mesproject.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;

    public DataTableDto getEquipmentData(MultiValueMap<String, String> formData) {
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
        Page<EquipmentDto> equipmentPage;

        if (searchType.equals("검색 조건") || StringUtils.isEmpty(searchValue)) {
            // 검색 조건이 없는 경우 전체 데이터 조회
            equipmentPage = equipmentRepository.findEquipment(pageable);
        } else {
            // 검색 조건이 있는 경우 해당 조건으로 데이터 조회
            equipmentPage = equipmentRepository.findEquipmentBySearchOption(
                    searchType,
                    searchValue,
                    pageable
            );
        }

        // DataTableDto에 결과 저장
        DataTableDto dto = new DataTableDto();
        dto.setDraw(draw);
        dto.setRecordsFiltered((int) equipmentPage.getTotalElements());
        dto.setRecordsTotal((int) equipmentRepository.count());
        dto.setData(equipmentPage.getContent());

        return dto;
    }

    @Transactional
    public void updateEquipmentStatus(List<Long> equipmentIds, List<EquipmentStatus> equipmentStatuses) {
        // 여기서는 각 장비의 상태를 받아와서 업데이트하는 예시입니다.
        for (int i = 0; i < equipmentIds.size(); i++) {
            Long equipmentId = equipmentIds.get(i);
            EquipmentStatus status = equipmentStatuses.get(i);

            // 상태에 따라 업데이트 로직을 추가할 수 있습니다.
            if ("AVAILABLE".equals(status.toString())) {
                equipmentRepository.updateEquipmentStatusToAvailable(equipmentId);
            } else if ("UNAVAILABLE".equals(status.toString())) {
                equipmentRepository.updateEquipmentStatusToUnavailable(equipmentId);
            } else {
                throw new IllegalArgumentException("Unexpected Equipment Status: " + status);
            }
        }
    }

}