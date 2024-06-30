package com.mesproject.service;

import com.mesproject.constant.OrdersStatus;
import com.mesproject.constant.WorkOrdersStatus;
import com.mesproject.entity.Orders;
import com.mesproject.entity.OrdersPlan;
import com.mesproject.entity.WorkPlan;
import com.mesproject.repository.OrdersPlanRepository;
import com.mesproject.repository.OrdersRepository;
import com.mesproject.repository.WorkPlanRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class WorkPlanServicce{
    private final WorkPlanRepository  workPlanRepository;
    private final OrdersPlanRepository  ordersPlanRepository;
    private final OrdersRepository ordersRepository;


    public void ProductionCompleted(WorkPlan workPlan){

        //수주-작업계획 테이블에서 작업계획 코드로 데이터 찾음 (ex.작업계획 10000번 완료)
        List<OrdersPlan> ordersPlanList = ordersPlanRepository.findByWorkPlan_WorkPlanId(workPlan.getWorkPlanId());
        List<Long> orderIdList = new ArrayList<>();
        List<Long> workPlanIdList = new ArrayList<>();
        boolean isCompleted = true;

        //해당하는 수주코드 리스트로 뽑힘.(ex. 1000번 , 1001번)
        for(OrdersPlan ordersPlan : ordersPlanList){
            orderIdList.add(ordersPlan.getOrders().getOrderId());}

        //수주-작업계획 테이블에서 수주코드로 데이터 찾음(ex.
        for(Long orderId : orderIdList){
            //1000번으로 수주-작업계획테이블에서 데이터 찾음.
            //해당하는 수주-작업계획 레코드 뽑힘
            List<OrdersPlan> ordersPlanList2 = ordersPlanRepository.findByOrders_OrderId(orderId);
            for(OrdersPlan ordersPlan2 : ordersPlanList2){
                //작업계획 코드 리스트로 뽑힘.( 10000번, 10001번)
                workPlanIdList.add(ordersPlan2.getWorkPlan().getWorkPlanId());
                //반복문 돌면서 그 작업계획이 완료되었는지 확인
                for(Long workplanId : workPlanIdList){
                   WorkPlan workplan =  workPlanRepository.findById(workplanId)











                           .orElseThrow(EntityNotFoundException::new);
                   if(workplan.getWorkPlanStatus()!= WorkOrdersStatus.COMPLETED){
                       isCompleted = false;
                   }


                }
            }
            if(isCompleted){

                Orders orders = ordersRepository.findById(orderId)
                        .orElseThrow(EntityNotFoundException::new);
                orders.setOrdersStatus(OrdersStatus.PRODUCTIONCOMPLETED);
            }
        }
        }
    }


