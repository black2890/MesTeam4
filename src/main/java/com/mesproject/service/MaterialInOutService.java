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
    private final ProductRepository productRepository;
    private final WorkOrdersRepository workOrdersRepository;


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
        LocalDateTime deliveryDate = materialOrders.getDeliveryDate();
        if(materialOrderDto.getStorageDate() ==null){
            materialOrderDto.setStorageDate(deliveryDate);
        }

        // storageDate가 deliveryDate 이후인지 확인

        if (materialOrderDto.getStorageDate().isBefore(deliveryDate)) {
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

    public void outpackaging(Long workOrderId,WorkPlan workPlan, LocalDateTime start, String worker) {
        Long productId = workPlan.getProduct().getProductId();
        //quantity 는 box 단위
        //즙의 경우, 포장지는 30*quantity 필요, box 는 quantity  필요
        //스틱 포장지 수 바꿔주기
        Long quantity = workPlan.getQuantity();
        Long  wrappingQuantity = 0L;
        if(productId == 1 || productId ==2){
            wrappingQuantity = quantity*30;
        }
        else if(productId == 3 || productId ==4){
            wrappingQuantity = quantity*25;
        }
            //자재 입출고 테이블에서 입고된 box 선입선출
            List<MaterialInOut> wrappingPaperList = materialInOutRepository.findByProductIdAndStatusOrderByStorageDateAsc(11L);
            List<MaterialInOut> boxList = materialInOutRepository.findByProductIdAndStatusOrderByStorageDateAsc(12L);

            for(MaterialInOut materialInOut : wrappingPaperList){

                if(wrappingQuantity >= materialInOut.getQuantity()){
                    WorkOrders workOrders = workOrdersRepository.findById(workOrderId)
                            .orElseThrow(EntityNotFoundException::new);
                    materialInOut.setWorkOrders(workOrders);
                    materialInOut.setMaterialInOutStatus(MaterialInOutStatus.RETRIEVAL);
                    materialInOut.setRetrievalWorker(worker);
                    materialInOut.setRetrievalDate(start);
                    wrappingQuantity-=materialInOut.getQuantity();
                    if(wrappingQuantity==0){break;}
                }else{
                    //기존 재고는 수량만 update
                    WorkOrders workOrders = workOrdersRepository.findById(workOrderId)
                            .orElseThrow(EntityNotFoundException::new);
                    materialInOut.setWorkOrders(workOrders);
                    materialInOut.setQuantity(materialInOut.getQuantity() - wrappingQuantity);
                    //출고한 재고는 출고시킨 수량으로 insert
                   MaterialInOut materialInOut1 = MaterialInOut.updateMaterialInOut(materialInOut, wrappingQuantity, start,worker);
                    materialInOutRepository.save(materialInOut1);
                    break;
                }

            }

            for(MaterialInOut materialInOut : boxList){
                if(quantity >= materialInOut.getQuantity()){
                    WorkOrders workOrders = workOrdersRepository.findById(workOrderId)
                            .orElseThrow(EntityNotFoundException::new);
                    materialInOut.setWorkOrders(workOrders);
                    materialInOut.setMaterialInOutStatus(MaterialInOutStatus.RETRIEVAL);
                    materialInOut.setRetrievalWorker(worker);
                    materialInOut.setRetrievalDate(start);
                    quantity-=materialInOut.getQuantity();
                    if(quantity==0){break;}
                }else{
                    //기존 재고는 수량만 update
                    WorkOrders workOrders = workOrdersRepository.findById(workOrderId)
                            .orElseThrow(EntityNotFoundException::new);
                    materialInOut.setWorkOrders(workOrders);
                    materialInOut.setQuantity(materialInOut.getQuantity() - quantity);
                    //출고한 재고는 출고시킨 수량으로 insert
                    MaterialInOut materialInOut1 = MaterialInOut.updateMaterialInOut(materialInOut, quantity, start,worker);
                    materialInOutRepository.save(materialInOut1);
                    break;
                }

            }
            orderPackaging(11L);
            orderPackaging(12L);




    }




    public void outMaterial(Long materialId, Long workOrderId, LocalDateTime start, String worker) {

        Long quantity = 0L;
        if(materialId ==5L || materialId ==6L){
            quantity= 1000L;
        }else if(materialId ==7L){
            quantity = 50L;
        }else if(materialId ==8L || materialId ==9L){
            quantity = 20L;
        }else if(materialId ==10L){
            quantity = 8L;
        }


        //자재 입출고 테이블에서 입고된 콜라겐 선입선출
        List<MaterialInOut> collagenList = materialInOutRepository.findByProductIdAndStatusOrderByStorageDateAsc(materialId);


        for(MaterialInOut materialInOut : collagenList){

            if(quantity >= materialInOut.getQuantity()){
                materialInOut.setMaterialInOutStatus(MaterialInOutStatus.RETRIEVAL);
                WorkOrders workOrders = workOrdersRepository.findById(workOrderId)
                        .orElseThrow(EntityNotFoundException::new);
                materialInOut.setWorkOrders(workOrders);
                materialInOut.setRetrievalWorker(worker);
                materialInOut.setRetrievalDate(start);
                quantity-=materialInOut.getQuantity();
                if(quantity==0){break;}

            }else{
                //기존 재고는 수량만 update
                WorkOrders workOrders = workOrdersRepository.findById(workOrderId)
                        .orElseThrow(EntityNotFoundException::new);
                materialInOut.setWorkOrders(workOrders);
                materialInOut.setQuantity(materialInOut.getQuantity() - quantity);
                //출고한 재고는 출고시킨 수량으로 insert
                MaterialInOut materialInOut1 = MaterialInOut.updateMaterialInOut(materialInOut, quantity, start,worker);
                materialInOutRepository.save(materialInOut1);
                break;
            }

        }

        if(materialId == 10L){
            orderPackaging(materialId);
        }





    }
    /*
    box, 포장지 출고 후 , 일정 수량 이하로 떨어지면 자동 발주하는 메서드
    1. 입출고 테이블에서 productid 로 데이터 전부조회 ( 상태=입고)
    2. 입고인 데이터들의 수량 세기
    3. 일정량 이하면 발주
     */

    public void orderPackaging(Long productId){
            Long quantity = materialInOutRepository.sumQuantityByProductIdAndStatus(productId);

            if(productId == 11L && quantity <=100000){

                Product product = productRepository.findById(productId)
                        .orElseThrow(EntityNotFoundException::new);
                MaterialOrderDto materialOrderDto = new MaterialOrderDto();
                materialOrderDto.setProductId(productId);
                materialOrderDto.setMaterialOrderDate(LocalDateTime.now());
                materialOrderDto.setQuantity(100000L);
                MaterialOrders materialOrders = MaterialOrders.createMaterialOrders(materialOrderDto,product);
                MaterialInOut materialInOut = MaterialInOut.createMaterialInOut(materialOrders);
                materialOrdersRepository.save(materialOrders);
                materialInOutRepository.save(materialInOut);
            }

            else if(productId == 12L && quantity <=10000){

            Product product = productRepository.findById(productId)
                    .orElseThrow(EntityNotFoundException::new);
            MaterialOrderDto materialOrderDto = new MaterialOrderDto();
            materialOrderDto.setProductId(productId);
            materialOrderDto.setMaterialOrderDate(LocalDateTime.now());
            materialOrderDto.setQuantity(10000L);
            MaterialOrders materialOrders = MaterialOrders.createMaterialOrders(materialOrderDto,product);
            MaterialInOut materialInOut = MaterialInOut.createMaterialInOut(materialOrders);
            materialOrdersRepository.save(materialOrders);
            materialInOutRepository.save(materialInOut);
        }
        else if(productId == 10L && quantity <=24L){

            Product product = productRepository.findById(productId)
                    .orElseThrow(EntityNotFoundException::new);
            MaterialOrderDto materialOrderDto = new MaterialOrderDto();
            materialOrderDto.setProductId(productId);
            materialOrderDto.setMaterialOrderDate(LocalDateTime.now());
            materialOrderDto.setQuantity(24L);
            MaterialOrders materialOrders = MaterialOrders.createMaterialOrders(materialOrderDto,product);
            MaterialInOut materialInOut = MaterialInOut.createMaterialInOut(materialOrders);
            materialOrdersRepository.save(materialOrders);
            materialInOutRepository.save(materialInOut);
        }


    }

}
