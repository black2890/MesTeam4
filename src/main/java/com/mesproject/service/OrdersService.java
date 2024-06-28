package com.mesproject.service;

import com.mesproject.constant.InventoryStatus;
import com.mesproject.constant.OrdersStatus;
import com.mesproject.dto.ShipmentDto;
import com.mesproject.entity.*;
import com.mesproject.repository.*;
import com.mesproject.dto.OrderDetailsDto;
import com.mesproject.entity.*;
import com.mesproject.repository.InventoryRepository;
import com.mesproject.repository.OrdersPlanRepository;
import com.mesproject.repository.OrdersRepository;
import com.mesproject.repository.WorkPlanRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@Service
public class OrdersService {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private  OrdersPlanRepository ordersPlanRepository;
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private WorkPlanRepository workPlanRepository;
    @Autowired
    private VendorRepository vendorRepository;


    public void updateOrderStatus(List<Long> orderIds, OrdersStatus status) {
        //출하이면, 출하업체 입력받아서 저장해야함
        //배송완료이면, 배송일 입력받아서 저장해야함
        ordersRepository.updateStatusByIds(orderIds, status);
        if(status ==OrdersStatus.RETRIEVALCOMPLETED){
            retrievalCompleted(orderIds);
        }
    }

    public List<Object[]> getProductOrdersSummary() {
        return ordersRepository.findProductOrdersSummary();
    }

    /*
    1. 수주의 수량만큼 재고 차감 (900)
    2. 수주-작업계획코드 테이블에서 수주코드로 작업계획코드 찾음 (리스트일 수도 있음) (1,2,3)
    3. 재고 테이블에서 작업계획 코드로 재고 조회후, 그 수량 차감 (1,2,3) (선입선출)
    ex) 333 + 333 + 234

    sol) quantity가 0이 될 때까지
    작업계획 코드로 재고 조회 후,
    1번 작업계획 코드로 재고 조회 후, 수주의 수량>=재고의 수량이면 전부 출고
    ...
    3번 작업계획 코드로 재고 조회 후 , 수주의 수량<재고의 수량이면 수량을 재고-수주의 수량으로 update
    나머지 데이터는 똑같고, quantity = 수주에보내야하는 수량, inventorystatus==출고로 레코드 insert

    +) "quantity가 0이 될 때까지"라는 조건 넣어야 하는지 잘 모르겠음
        수주랑 엮여있는 작업계획만 조회하니까 굳이 조건 없어도 마지막까지 가야 수량이 전부 채워짐.


     */
    public void retrievalCompleted(List<Long> orderIds){
        List<Long> workPlanIdList = new ArrayList<>();

        for(Long orderId : orderIds){
            Orders orders = ordersRepository.findById(orderId)
                    .orElseThrow(EntityNotFoundException::new);
            Long quantity = orders.getQuantity();
            Long productId = orders.getProduct().getProductId();
//            if(productId==1 || productId==2){
//                if(quantity ==1000){
//                    quantity =999L;
//                }
//            }
            List<OrdersPlan> ordersPlanList = ordersPlanRepository.findByOrders_OrderIdOrderByOrders_OrderIdAsc(orderId);
            // 수주코드는 무조건 하나의 작업계획이라도 참조하게 되어있음.
            //아닌 경우 생기면 추후 고려
            for(OrdersPlan ordersPlan : ordersPlanList){
                workPlanIdList.add(ordersPlan.getWorkPlan().getWorkPlanId());
            }
            //
            for(Long workPlanId : workPlanIdList){
                WorkPlan workPlan = workPlanRepository.findById(workPlanId)
                        .orElseThrow(EntityNotFoundException::new);
                Inventory inventory = inventoryRepository.findByWorkPlan_WorkPlanId(workPlanId);
                if(quantity >= inventory.getQuantity()){
                    inventory.setInventoryStatus(InventoryStatus.RETRIEVALCOMPLETED);
                    quantity-=inventory.getQuantity();
                }else{
                    //기존 재고는 수량만 update
                    inventory.setQuantity(inventory.getQuantity() - quantity);
                    //출고한 재고는 출고시킨 수량으로 insert
                   Inventory  retrievalInventory = Inventory.updateInventory(workPlan, quantity);
                   inventoryRepository.save(retrievalInventory);
                }

            }

        }
    }
    public List<Orders> getCompletedOrders() {
        List<OrdersStatus> statuses = Arrays.asList(
                OrdersStatus.PRODUCTIONCOMPLETED,
                OrdersStatus.RETRIEVALCOMPLETED,
                OrdersStatus.DELIVERED
        );
        return ordersRepository.findByOrdersStatusIn(statuses);
    }

    public void shipmentStart(ShipmentDto shipmentDto) {
        //출하이면, 출하업체 입력받아서 저장해야함
        //배송완료이면, 배송일 입력받아서 저장해야함
        Orders orders = ordersRepository.findById(shipmentDto.getOrderId())
                .orElseThrow(EntityNotFoundException::new);

        orders.setOrdersStatus(OrdersStatus.RETRIEVALCOMPLETED);
        Vendor vendor = vendorRepository.findByVendorName(shipmentDto.getVendorName());
        orders.setDeliveryVendor(vendor);
        retrievalCompletedOne(shipmentDto.getOrderId(), shipmentDto.getWorker());

    }

    public void shipmentEnd(ShipmentDto shipmentDto) {
        //출하이면, 출하업체 입력받아서 저장해야함
        //배송완료이면, 배송일 입력받아서 저장해야함
        Orders orders = ordersRepository.findById(shipmentDto.getOrderId())
                .orElseThrow(EntityNotFoundException::new);
        orders.setOrdersStatus(OrdersStatus.DELIVERED);
        orders.setActualDeliveryDate(shipmentDto.getActualDeliveryDate());

    }

    public void retrievalCompletedOne(Long orderId , String worker) {
        List<Long> workPlanIdList = new ArrayList<>();


        Orders orders = ordersRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        Long quantity = orders.getQuantity();
        List<OrdersPlan> ordersPlanList = ordersPlanRepository.findByOrders_OrderIdOrderByOrders_OrderIdAsc(orderId);
        // 수주코드는 무조건 하나의 작업계획이라도 참조하게 되어있음.
        //아닌 경우 생기면 추후 고려
        for (OrdersPlan ordersPlan : ordersPlanList) {
            workPlanIdList.add(ordersPlan.getWorkPlan().getWorkPlanId());
        }
        //
        for (Long workPlanId : workPlanIdList) {
            WorkPlan workPlan = workPlanRepository.findById(workPlanId)
                    .orElseThrow(EntityNotFoundException::new);
            Inventory inventory = inventoryRepository.findByWorkPlan_WorkPlanId(workPlanId);
            if (quantity >= inventory.getQuantity()) {
                inventory.setInventoryStatus(InventoryStatus.RETRIEVALCOMPLETED);
                inventory.setRetrievalDate(LocalDateTime.now());
                inventory.setRetrievalWorker(worker);
                quantity -= inventory.getQuantity();
            } else {
                //기존 재고는 수량만 update
                inventory.setQuantity(inventory.getQuantity() - quantity);
                //출고한 재고는 출고시킨 수량으로 insert
                Inventory retrievalInventory = Inventory.updateInventory(workPlan, quantity);
                inventory.setRetrievalDate(LocalDateTime.now());
                inventory.setRetrievalWorker(worker);
                inventoryRepository.save(retrievalInventory);
            }

        }
    }






    public List<Map<String, Object>> getOrderDetails(Long orderId) {
        return ordersRepository.findRelatedDataByOrderId(orderId);
    }
}
