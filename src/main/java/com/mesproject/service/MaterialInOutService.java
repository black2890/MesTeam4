package com.mesproject.service;

import com.mesproject.dto.MaterialInOutDto;
import com.mesproject.entity.MaterialInOut;
import com.mesproject.repository.MaterialInOutRepository;
import com.mesproject.repository.OrdersRepository;
import com.mesproject.repository.WorkOrdersRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MaterialInOutService {
    private final MaterialInOutRepository materialInOutRepository;

   /*
    입고 메서드
    입출고코드 들고와서 상태 바꿔주기
    발주일 기준 원자재 lead time 지났는지 유효성 검사(원자재 종류에 따라)
  */
    public void In(MaterialInOutDto materialInOutDto){
        MaterialInOut materialInOut = materialInOutRepository.findById(materialInOutDto.getInoutId())
                .orElseThrow(EntityNotFoundException::new);
        materialInOut.setStorageDate(materialInOutDto.getStorageDate());
        materialInOut.setStorageWorker(materialInOutDto.getStorageWorker());

    }

    /*
    출고 메서드
    입출고코드 들고와서 상태 바꿔주기
    출고 전 해당 자재 입고되었는지 유효성 검사
     */
    public void Out(Long workOrderId, LocalDateTime start, String worker){
        List<MaterialInOut> materialInOutList = materialInOutRepository.findByWorkOrders_WorkOrderId(workOrderId);
        for(MaterialInOut materialInOut : materialInOutList){
            materialInOut.setRetrievalDate(start);
            materialInOut.setRetrievalWorker(worker);
        }



    }
}
