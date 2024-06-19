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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public Long order(OrderDto orderDto){


        //양배추즙 333box라고 가정 --> 이후 변경 필요
        Product product = productRepository.findById(1L)
                .orElseThrow(EntityNotFoundException::new);

        Vendor vendor = vendorRepository.findById(1L)
                .orElseThrow(EntityNotFoundException::new);
        /*
        수량에 따라, 발주량, 생산계획 조정 필요
         */
        /* 주문받은 productid가 1이면, 양배추, 벌꿀 발주
                              2이면, 흑마늘, 벌꿀 발주
                              3이면, 석류, 콜라겐 발주
                              4이면, 매실, 콜라겐 발주
         */
        Long materialId1 = 5L;
        Long materialId2 = 7L;
        // 납품예정일 저장해야함(지금 기준 이틀 뒤?, 원래는 생산 일정에 맞춰 발주하는 게 맞음)--> 첫 생산계획 3일 전?
        //양배추 발주
        MaterialOrderDto materialOrderDto1 = new MaterialOrderDto();
        materialOrderDto1.setProductId(materialId1);
        materialOrderDto1.setQuantity(1000L);

        Product product1 = productRepository.findById(materialId1)
                .orElseThrow(EntityNotFoundException::new);
        MaterialOrders materialOrders1 = MaterialOrders.createMaterialOrders(materialOrderDto1,product1);
        MaterialInOut materialInOut1 = MaterialInOut.createMaterialInOut(materialOrders1);
        materialOrdersRepository.save(materialOrders1);
        materialInOutRepository.save(materialInOut1);

        //벌꿀 발주
        MaterialOrderDto materialOrderDto2 = new MaterialOrderDto();
        materialOrderDto2.setProductId(materialId2);
        materialOrderDto2.setQuantity(50L);

        Product product2 = productRepository.findById(materialId2)
                .orElseThrow(EntityNotFoundException::new);
        MaterialOrders materialOrders2 = MaterialOrders.createMaterialOrders(materialOrderDto2, product2);
        MaterialInOut materialInOut2 = MaterialInOut.createMaterialInOut(materialOrders2);
        materialOrdersRepository.save(materialOrders2);
        materialInOutRepository.save(materialInOut2);

        //수주-발주 엔티티 생성 : 수주코드 저장은 cascade
        List<OrdersMaterials> ordersMaterialsList = new ArrayList<>();

        OrdersMaterials ordersMaterials1 = OrdersMaterials.createOrdersMaterials(materialOrders1);
        OrdersMaterials ordersMaterials2 = OrdersMaterials.createOrdersMaterials(materialOrders2);
        ordersMaterialsList.add(ordersMaterials1);
        ordersMaterialsList.add(ordersMaterials2);

        //작업계획 생성
        /*
        productid가 1,2이면 2일
        productid가 3,4이면 1일
         */

        WorkPlan workPlan = WorkPlan.createWorkPlan(product,orderDto.getQuantity());
        workPlan.setStart(LocalDateTime.now());
        workPlan.setEnd(LocalDateTime.now().plusDays(1));

        //수주-작업계획 엔티티 생성 : 수주코드 저장은 cascade
        List<OrdersPlan> ordersPlanList = new ArrayList<>();
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
            if(workOrders.getProcessType()== ProcessType.FILLING){
                materialInOut2.setWorkOrders(workOrders);

            }
        }


        //수주 생성
        Orders order = Orders.createOrder(ordersMaterialsList,ordersPlanList ,product,vendor,orderDto);
        ordersRepository.save(order);


        return order.getOrderId();
    }

}
