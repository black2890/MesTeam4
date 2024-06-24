package com.mesproject.service;

import com.mesproject.constant.ProcessType;
import com.mesproject.dto.MaterialOrderDto;
import com.mesproject.dto.OrderDto;
import com.mesproject.entity.*;
import com.mesproject.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.Order;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final MaterialInOutService materialInOutService;
    private final ProductRepository productRepository;
    private final WorkOrdersService workOrdersService;
    private final OrdersRepository ordersRepository;
    private final MaterialOrdersRepository materialOrdersRepository;
    private final MaterialInOutRepository materialInOutRepository;
    private final WorkPlanRepository workPlanRepository;
    private final VendorRepository vendorRepository;

    /*납품일 받았을 때, 생산계획 비어있다고 생각하고 넣는 메서드
    만약, 생산계획 차있으면 그 자리는 피해서 넣어야함.
    스틱은 있으면 건너뛰고 하루에 할당하면 되는데
    즙은 하루만 비어있으면 그 때는 생산 못하고 이틀 비어있는 곳에 넣어야 함.
     */
    public Long order(OrderDto orderDto){


        //양배추즙 333box라고 가정 --> 이후 변경 필요

        Product product = productRepository.findById(orderDto.getProductId())
                .orElseThrow(EntityNotFoundException::new);

        Vendor vendor = vendorRepository.findById(orderDto.getVendorId())
                .orElseThrow(EntityNotFoundException::new);
        /*
        수량에 따라, 발주량, 생산계획 조정 필요
         */
        /* 주문받은 productid가 1이면, 양배추, 벌꿀 발주
                              2이면, 흑마늘, 벌꿀 발주
                              3이면, 석류, 콜라겐 발주
                              4이면, 매실, 콜라겐 발주
         */

        // 생산횟수 계산
        int numberOfProduction=0;
        int maxQuantity=0;


        if(product.getProductId()==1 || product.getProductId()==2){
            maxQuantity=333;
            if(orderDto.getQuantity() ==1000){
                orderDto.setQuantity(999L);
            }

        }else if(product.getProductId()==3|| product.getProductId()==4){
            maxQuantity=160;

        }
        if(orderDto.getQuantity()%maxQuantity ==0){
            numberOfProduction = (int)(orderDto.getQuantity()/maxQuantity);
        }else {
            numberOfProduction = (int)(orderDto.getQuantity()/maxQuantity)+1;
        }


        //생산 시작일시 계산
        //납품일에 맞춰서 생산일시 계산하는 로직필요
        int productionPeriod = 0;
        LocalDateTime deliveryDate = orderDto.getDeliveryDate();
        LocalDateTime productionDate = null;

        if(product.getProductId()==1 || product.getProductId()==2){
            productionPeriod=2;
            productionDate = deliveryDate.minusDays((long) productionPeriod *numberOfProduction/2 + 2);
        }else if(product.getProductId()==3|| product.getProductId()==4){
            productionPeriod=1;
            productionDate = deliveryDate.minusDays(productionPeriod*numberOfProduction + 2);
        }




        Long materialId1=null;
        Long materialId2=null;
        if(orderDto.getProductId()==1){
            materialId1=5L;
            materialId2=7L;
        }else if(orderDto.getProductId()==2){
            materialId1=6L;
            materialId2=7L;
        }else if(orderDto.getProductId()==3){
            materialId1=8L;
            materialId2=10L;
        }else if(orderDto.getProductId()==4){
            materialId1=9L;
            materialId2=10L;
        }

        // 납품예정일 저장해야함(지금 기준 이틀 뒤?, 원래는 생산 일정에 맞춰 발주하는 게 맞음)--> 첫 생산계획 3일 전?
        MaterialOrderDto materialOrderDto1 = new MaterialOrderDto();
        materialOrderDto1.setProductId(materialId1);
        materialOrderDto1.setMaterialOrderDate(productionDate.minusDays(7));
   //     materialOrderDto1.setDeliveryDate(productionDate);
        if(orderDto.getProductId()==1 || orderDto.getProductId()==2){
            materialOrderDto1.setQuantity(1000L);
        }else if(orderDto.getProductId()==3||orderDto.getProductId()==4){
            materialOrderDto1.setQuantity(20L);
        }
        MaterialOrderDto materialOrderDto2 = new MaterialOrderDto();
        materialOrderDto2.setProductId(materialId2);
        materialOrderDto2.setMaterialOrderDate(productionDate.minusDays(7));
      //  materialOrderDto2.setDeliveryDate(productionDate);
        if(orderDto.getProductId()==1 || orderDto.getProductId()==2){
            materialOrderDto2.setQuantity(50L);
        }else if(orderDto.getProductId()==3||orderDto.getProductId()==4){
            materialOrderDto2.setQuantity(20L);
        }



      //수주-발주 리스트 생성
        // 수주-작업계획 리스트 생성
        List<OrdersMaterials> ordersMaterialsList = new ArrayList<>();
        List<OrdersPlan> ordersPlanList = new ArrayList<>();

        int count =1;
        for(int i=0;i<numberOfProduction;i++){

            //발주, 입출고 엔티티생성
            Product product1 = productRepository.findById(materialId1)
                    .orElseThrow(EntityNotFoundException::new);
            MaterialOrders materialOrders1 = MaterialOrders.createMaterialOrders(materialOrderDto1,product1);
            MaterialInOut materialInOut1 = MaterialInOut.createMaterialInOut(materialOrders1);
            materialOrdersRepository.save(materialOrders1);
            materialInOutRepository.save(materialInOut1);


            Product product2 = productRepository.findById(materialId2)
                    .orElseThrow(EntityNotFoundException::new);
            MaterialOrders materialOrders2 = MaterialOrders.createMaterialOrders(materialOrderDto2, product2);
            MaterialInOut materialInOut2 = MaterialInOut.createMaterialInOut(materialOrders2);
            materialOrdersRepository.save(materialOrders2);
            materialInOutRepository.save(materialInOut2);

            //수주-발주 엔티티 생성 : 수주코드 저장은 createorder 에서
            OrdersMaterials ordersMaterials1 = OrdersMaterials.createOrdersMaterials(materialOrders1);
            OrdersMaterials ordersMaterials2 = OrdersMaterials.createOrdersMaterials(materialOrders2);
            ordersMaterialsList.add(ordersMaterials1);
            ordersMaterialsList.add(ordersMaterials2);


            //작업계획 생성
        /*
        productid가 1,2이면 2일
        productid가 3,4이면 1일
         */

            /*
            ex) quantity = 667
            1) count =1, numberOfProduction =3
            workplan1 : 333

            2) count =2,numberOfProduction =3
            workplan2 : 333

            3) count=3, numberofProduction =3
            workplan3: 1


            ex) quantity =666
            1) count =1 ,  numberofProduction = 2
            workplan1 :333

            2) count =2, numberofProduction =2
            workplan2 : 333


            ex) quantity = 1000
            1) count = 1 ,numberofProduction =3
            workplan1 : 333
            2) count = 2 ,numberofProduction =3
            workplan2 : 333
            3) count = 3, numberofProduction =3
            workplan3: 334

            현재방식의 문제점 : workplan 3 을 333으로 저장해버림. 실제 생산량과 괴리 있음.

             */
            WorkPlan workPlan;
            if(count==numberOfProduction){
                //처음에 count 가 1, numberofProduction 이 1
                if(orderDto.getQuantity()%maxQuantity==0){
                    workPlan = WorkPlan.createWorkPlan(product, (long) maxQuantity);
                }else{
                    workPlan = WorkPlan.createWorkPlan(product,orderDto.getQuantity()%maxQuantity);
                }

            }else if(numberOfProduction ==1){
                workPlan = WorkPlan.createWorkPlan(product,orderDto.getQuantity());
            }
            else{
                workPlan = WorkPlan.createWorkPlan(product, (long) maxQuantity);

            }
            count++;
            //productiondate를 무작정 +2 or +1 해주는 건 안됨. 사이에 작업계획 있는지 확인해야
            workPlan.setStart(productionDate);
            if(orderDto.getProductId()==1 || orderDto.getProductId()==2){
                workPlan.setEnd(productionDate.plusHours(44));
                if(count!=1&& count%2==1){
                productionDate = productionDate.plusDays(2);}
            }else if(orderDto.getProductId()==3||orderDto.getProductId()==4){
                workPlan.setEnd(productionDate.plusHours(22));
                productionDate = productionDate.plusDays(1);
            }

            //수주-작업계획 엔티티 생성 :  수주코드 저장은 createorder 에서
            OrdersPlan ordersPlan = OrdersPlan.createOrdersPlan(workPlan);
            ordersPlanList.add(ordersPlan);

            //작업계획에 따른 작업지시 생성 --> 추후 수량에 따라 작업계획 여러개 생기면,각각의 계획에 모두 생산지시 리스트로 저장해줘야함.
            List<WorkOrders> workOrdersList = workOrdersService.createWorkOrders(workPlan);
            workPlan.setWorkOrders(workOrdersList);
            workPlanRepository.save(workPlan);

                      /*
        양배추, 흑마늘에 세척 저장
        벌꿀에 충진 저장

         */
            for(WorkOrders workOrders : workOrdersList){
                if(workOrders.getProcessType()== ProcessType.CLEANING){
                    materialInOut1.setWorkOrders(workOrders);
                }
                else if(workOrders.getProcessType()== ProcessType.FILLING){
                    materialInOut2.setWorkOrders(workOrders);

                }
                else if(workOrders.getProcessType()== ProcessType.MIX){
                    materialInOut1.setWorkOrders(workOrders);
                    materialInOut2.setWorkOrders(workOrders);
                }
            }



        }

        //수주 생성
        Orders order = Orders.createOrder(ordersMaterialsList,ordersPlanList ,product,vendor,orderDto);
        ordersRepository.save(order);


        return order.getOrderId();
    }





}
