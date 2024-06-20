package com.mesproject.service;

import com.mesproject.constant.MaterialInOutStatus;
import com.mesproject.constant.MaterialOrdersStatus;
import com.mesproject.dto.MaterialInOutDto;
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
//    public void In(MaterialInOutDto materialInOutDto){
//        MaterialInOut materialInOut = materialInOutRepository.findById(materialInOutDto.getInoutId())
//                .orElseThrow(EntityNotFoundException::new);
//        MaterialOrders materialOrders = materialOrdersRepository.findById(materialInOut.getMaterialOrders().getMaterialOrderId())
//                .orElseThrow(EntityNotFoundException::new);
//
//        // deliveryDate 가져오기
//        LocalDate deliveryDate = materialOrders.getDeliveryDate();
//
//        // storageDate가 deliveryDate 이후인지 확인
//        if (materialInOutDto.getStorageDate().isBefore(deliveryDate.atStartOfDay())) {
//            throw new IllegalArgumentException("발주일 기준, 원자재 lead time 이 지나지 않았습니다.");
//        }
//
//        materialInOut.setStorageDate(materialInOutDto.getStorageDate());
//        materialInOut.setStorageWorker(materialInOutDto.getStorageWorker());
//        materialOrders.setMaterialOrdersStatus(MaterialOrdersStatus.STORAGECOMPLETED);
//
//    }

    public void In(Long MaterialOrderId){

        MaterialOrders materialOrders = materialOrdersRepository.findById(MaterialOrderId)
                .orElseThrow(EntityNotFoundException::new);

        MaterialInOut materialInOut = materialInOutRepository.findByMaterialOrders_MaterialOrderId(MaterialOrderId)
                .orElseThrow(EntityNotFoundException::new);

        // deliveryDate 가져오기
       // LocalDate deliveryDate = materialOrders.getDeliveryDate();

        // storageDate가 deliveryDate 이후인지 확인
//        if (materialInOutDto.getStorageDate().isBefore(deliveryDate.atStartOfDay())) {
//            throw new IllegalArgumentException("발주일 기준, 원자재 lead time 이 지나지 않았습니다.");
//        }

//        materialInOut.setStorageDate(materialInOutDto.getStorageDate());
//        materialInOut.setStorageWorker(materialInOutDto.getStorageWorker());
        materialOrders.setMaterialOrdersStatus(MaterialOrdersStatus.STORAGECOMPLETED);
        materialInOut.setMaterialInOutStatus(MaterialInOutStatus.STORAGE);

    }

    /*
    출고 메서드
    입출고코드 들고와서 상태 바꿔주기
    출고 전 해당 자재 입고되었는지 유효성 검사
     */
    public void Out(Long workOrderId, LocalDateTime start, String worker){
        List<MaterialInOut> materialInOutList = materialInOutRepository.findByWorkOrders_WorkOrderId(workOrderId);
        for(MaterialInOut materialInOut : materialInOutList){
            materialInOut.setMaterialInOutStatus(MaterialInOutStatus.RETRIEVAL);
        }
//        for(MaterialInOut materialInOut : materialInOutList){
//            materialInOut.setRetrievalDate(start);
//            materialInOut.setRetrievalWorker(worker);
//        }



    }
}
