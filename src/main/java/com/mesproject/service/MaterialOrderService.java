package com.mesproject.service;

import com.mesproject.constant.MaterialOrdersStatus;
import com.mesproject.entity.MaterialOrders;
import com.mesproject.repository.MaterialInOutRepository;
import com.mesproject.repository.MaterialOrdersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MaterialOrderService {

    private final MaterialInOutRepository materialInOutRepository;
    private final MaterialOrdersRepository materialOrdersRepository;

    public List<MaterialOrders> searchDateReplies(String condition, String start, String end) {
        LocalDateTime startDateTime = convertStringToLocalDateTime(start, false);

        if (end == null || end.isEmpty()) {
            if (condition.equals("deletetime")) {//발주일
                return materialOrdersRepository.findByMaterialOrderDate(startDateTime);
            } else if (condition.equals("recovertime")) {//납품일
                return materialOrdersRepository.findByMaterialDeliveryDate(startDateTime);
            }
        } else {
            LocalDateTime endDateTime = convertStringToLocalDateTime(end, true);

            // 기존의 날짜 범위 검색 메서드 호출
            if (condition.equals("deletetime")) {//발주일
                return materialOrdersRepository.findByMaterialOrderDateBetween(startDateTime, endDateTime);
            } else if (condition.equals("recovertime")) {//납품일
                return materialOrdersRepository.findByMaterialDeliveryDateBetween(startDateTime, endDateTime);
            }
        }

        // 검색 조건이 잘못된 경우 처리
        throw new IllegalArgumentException("Invalid search condition: " + condition);
    }

    // 댓글 검색
    public List<MaterialOrders> searchStringReplies(String condition, String keyword) {
        if (condition.equals("content")) {
            return materialOrdersRepository.findByNameContaining(keyword);
        } else {
            // 검색 조건이 잘못된 경우 처리
            throw new IllegalArgumentException("Invalid search condition: " + condition);
        }
    }

    // 날짜 문자열을 LocalDateTime으로 변환하는 유틸리티 메서드
    private LocalDateTime convertStringToLocalDateTime(String dateStr, boolean isEndOfDay) {
        LocalDate localDate = LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE);
        return isEndOfDay ? LocalDateTime.of(localDate, LocalTime.MAX) : LocalDateTime.of(localDate, LocalTime.MIN);
    }


    // 댓글관리 라디오 검색
    public List<MaterialOrders> searchRadioReplyBoards(String replyCondition){

        if(replyCondition.equals("PENDINGSTORAGE" )){
            return materialOrdersRepository.findByMaterialConditionContaining(MaterialOrdersStatus.PENDINGSTORAGE);
        }else{
            return materialOrdersRepository.findByMaterialConditionContaining(MaterialOrdersStatus.STORAGECOMPLETED);
        }

    }
}
