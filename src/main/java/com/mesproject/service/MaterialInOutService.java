package com.mesproject.service;

import com.mesproject.constant.MaterialInOutStatus;
import com.mesproject.constant.MaterialOrdersStatus;
import com.mesproject.dto.MaterialInOutDto;
import com.mesproject.dto.MaterialOrderDto;
import com.mesproject.entity.MaterialInOut;
import com.mesproject.entity.MaterialOrders;
import com.mesproject.repository.MaterialInOutRepository;
import com.mesproject.repository.MaterialOrdersRepository;
import com.mesproject.repository.OrdersRepository;
import com.mesproject.repository.WorkOrdersRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MaterialInOutService {
    private final MaterialInOutRepository materialInOutRepository;
    private final MaterialOrdersRepository materialOrdersRepository;

   /*
    입고 메서드
    입출고코드 들고와서 상태 바꿔주기
    발주일 기준 원자재 lead time 지났는지 유효성 검사(원자재 종류에 따라)
  */
    public void In(MaterialOrderDto materialOrderDto){

        MaterialOrders materialOrders = materialOrdersRepository.findById(materialOrderDto.getMaterialOrderId())
                .orElseThrow(EntityNotFoundException::new);

        MaterialInOut materialInOut = materialInOutRepository.findByMaterialOrders_MaterialOrderId(materialOrderDto.getMaterialOrderId())
                .orElseThrow(EntityNotFoundException::new);

        // deliveryDate 가져오기
        LocalDate deliveryDate = materialOrders.getDeliveryDate();

        // storageDate가 deliveryDate 이후인지 확인
        if (materialOrderDto.getStorageDate().isBefore(deliveryDate.atStartOfDay())) {
            throw new IllegalArgumentException("발주일 기준, 원자재 lead time 이 지나지 않았습니다.");
        }
        //입고 데이터에 입고일, 입고자, 입고상태 저장
        materialInOut.setStorageDate(materialOrderDto.getStorageDate());
        materialInOut.setStorageWorker(materialOrderDto.getStorageWorker());
        materialInOut.setMaterialInOutStatus(MaterialInOutStatus.STORAGE);
        //발주 데이터 입고 완료처리
        materialOrders.setMaterialOrdersStatus(MaterialOrdersStatus.STORAGECOMPLETED);

    }


    /*
    출고 메서드
    입출고코드 들고와서 상태 바꿔주기
    출고 전 해당 자재 입고되었는지 유효성 검사
     */
    public void Out(Long workOrderId, LocalDateTime start, String worker){
        List<MaterialInOut> materialInOutList = materialInOutRepository.findByWorkOrders_WorkOrderId(workOrderId);
        for(MaterialInOut materialInOut : materialInOutList){
            if (materialInOut.getMaterialInOutStatus()!=MaterialInOutStatus.STORAGE) {
                throw new IllegalArgumentException("자재가 입고되지 않았습니다.");
            }

            materialInOut.setMaterialInOutStatus(MaterialInOutStatus.RETRIEVAL);
            materialInOut.setRetrievalDate(start);
            materialInOut.setRetrievalWorker(worker);
            materialInOutRepository.save(materialInOut);
        }



    }
}
