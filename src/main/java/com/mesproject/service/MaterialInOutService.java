package com.mesproject.service;

import com.mesproject.constant.MaterialInOutStatus;
import com.mesproject.constant.MaterialOrdersStatus;
import com.mesproject.constant.OrdersStatus;
import com.mesproject.constant.WorkOrdersStatus;
import com.mesproject.dto.MaterialInOutDto;
import com.mesproject.dto.MaterialOrderDto;
import com.mesproject.entity.*;
import com.mesproject.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MaterialInOutService {
    private final MaterialInOutRepository materialInOutRepository;
    private final MaterialOrdersRepository materialOrdersRepository;
    private  final OrdersRepository ordersRepository;
    private final OrdersMaterialsRepository ordersMaterialsRepository;


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
        storageCompleted(materialOrders);

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
    /*
    1. 수주-발주 테이블에서 수주코드로 데이터 찾음

    2. 해당하는 발주코드 리스트로 뽑힘

    3. 발주테이블에서 상태 입고완료인지 확인

     */

//    public void storageCompleted(Orders orders){
//        Long orderId = orders.getOrderId();
//        List<OrdersMaterials> ordersMaterialsList = ordersMaterialsRepository.findByOrders_OrderId(orderId);
//        List<Long> materialIdList = new ArrayList<>();
//        boolean isCompleted = true;
//        for(OrdersMaterials ordersMaterials : ordersMaterialsList){
//            materialIdList.add(ordersMaterials.getMaterialOrders().getMaterialOrderId());
//        }
//        for(Long id : materialIdList){
//            MaterialOrders materialOrders = materialOrdersRepository.findById(id)
//                    .orElseThrow(EntityNotFoundException::new);
//            if(materialOrders.getMaterialOrdersStatus() != MaterialOrdersStatus.STORAGECOMPLETED){
//                isCompleted = false;
//            }
//        }
//        if(isCompleted){
//            orders.setOrdersStatus(OrdersStatus.STORAGECOMPLETED);
//        }
//
//    }

    //list null 처리 아직 안 함
    public void storageCompleted(MaterialOrders materialOrders){

        //수주-발주 테이블에서 발주 코드로 데이터 찾음 ( ex. 10000번)
        List<OrdersMaterials> ordersMaterialsList = ordersMaterialsRepository.findByMaterialOrders_MaterialOrderId(materialOrders.getMaterialOrderId());
        List<Long> orderIdList = new ArrayList<>();
        List<Long> materialIdList = new ArrayList<>();
        boolean isCompleted = true;

        //해당하는 수주코드 리스트로 뽑힘.(ex. 1000번 , 1001번)
        for(OrdersMaterials ordersMaterials : ordersMaterialsList){
            orderIdList.add(ordersMaterials.getOrders().getOrderId());}

        //수주-발주 테이블에서 수주코드로 데이터 찾음(ex.
        for(Long orderId : orderIdList){
            //1000번으로 수주-발주테이블에서 데이터 찾음.
            //해당하는 수주-발주 레코드 뽑힘
            List<OrdersMaterials> ordersMaterialsList2 = ordersMaterialsRepository.findByOrders_OrderId(orderId);
            for(OrdersMaterials ordersMaterials2 : ordersMaterialsList2){
                //발주 코드 리스트로 뽑힘.( 10000번, 10001번)
                materialIdList.add(ordersMaterials2.getMaterialOrders().getMaterialOrderId());
                //반복문 돌면서 그 발주가 입고되었는지 확인. break로 빠져나가지 않았으면 입고완료 처리
                for(Long materialId : materialIdList){
                    MaterialOrders materialOrders1 =  materialOrdersRepository.findById(materialId)
                            .orElseThrow(EntityNotFoundException::new);
                    if(materialOrders1.getMaterialOrdersStatus()!= MaterialOrdersStatus.STORAGECOMPLETED){
                        isCompleted = false;
                    }


                }

            }
            if(isCompleted){

                Orders orders = ordersRepository.findById(orderId)
                        .orElseThrow(EntityNotFoundException::new);
                orders.setOrdersStatus(OrdersStatus.STORAGECOMPLETED);
            }
        }
    }
}
