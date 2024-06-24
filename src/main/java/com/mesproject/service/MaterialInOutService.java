package com.mesproject.service;

import com.mesproject.constant.*;
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
    private final WorkPlanRepository workPlanRepository;


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
        materialInOut.setExpirationDate(materialOrderDto.getStorageDate().plusMonths(6));
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
        if(materialInOutList != null && materialInOutList.size() > 0){
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

    public void outpackaging(WorkPlan workPlan, LocalDateTime start, String worker) {
        Long productId = workPlan.getProduct().getProductId();
        //quantity 는 box 단위
        //즙의 경우, 포장지는 30*quantity 필요, box 는 quantity  필요
        //
        Long quantity = workPlan.getQuantity();

        if(productId == 1 || productId ==2){
            //자재 입출고 테이블에서 입고된 box 선입선출
            List<MaterialInOut> wrappingPaperList = materialInOutRepository.findAllByProduct_ProductIdOrderByStorageDateAsc(11L);
            List<MaterialInOut> boxList = materialInOutRepository.findAllByProduct_ProductIdOrderByStorageDateAsc(12L);

            for(MaterialInOut materialInOut : wrappingPaperList){
               Long  wrappingQuantity = quantity*30;
                if(wrappingQuantity >= materialInOut.getQuantity()){
                    materialInOut.setMaterialInOutStatus(MaterialInOutStatus.RETRIEVAL);
                    wrappingQuantity-=materialInOut.getQuantity();
                    if(wrappingQuantity==0){break;}
                }else{
                    //기존 재고는 수량만 update
                    materialInOut.setQuantity(materialInOut.getQuantity() - wrappingQuantity);
                    //출고한 재고는 출고시킨 수량으로 insert
                   MaterialInOut materialInOut1 = MaterialInOut.updateMaterialInOut(materialInOut, wrappingQuantity, start,worker);
                    materialInOutRepository.save(materialInOut1);
                    break;
                }

            }

            for(MaterialInOut materialInOut : boxList){
                if(quantity >= materialInOut.getQuantity()){
                    materialInOut.setMaterialInOutStatus(MaterialInOutStatus.RETRIEVAL);
                    quantity-=materialInOut.getQuantity();
                    if(quantity==0){break;}
                }else{
                    //기존 재고는 수량만 update
                    materialInOut.setQuantity(materialInOut.getQuantity() - quantity);
                    //출고한 재고는 출고시킨 수량으로 insert
                    MaterialInOut materialInOut1 = MaterialInOut.updateMaterialInOut(materialInOut, quantity, start,worker);
                    materialInOutRepository.save(materialInOut1);
                    break;
                }

            }
        }

    }
}
