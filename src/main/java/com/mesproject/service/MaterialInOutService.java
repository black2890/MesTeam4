package com.mesproject.service;

import com.mesproject.repository.OrdersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class MaterialInOutService {
   /*
   입고 메서드
   갖고온 발주번호로 입출고 엔티티 생성
   수주-발주 테이블에서 발주코드로 수주코드조회
   수주-작업계획 테이블에서 수주코드로 작업계획 코드 조회

    작업지시에서 (작업계획코드==? && 작업명==? )
    발주코드에 있는 제품코드가 5이면, 작업명 : 세척
    ...

    발주코드, 작업지시코드 등록해 입고완료
    */
}
