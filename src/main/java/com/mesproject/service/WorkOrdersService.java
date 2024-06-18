package com.mesproject.service;

import com.mesproject.constant.ProcessType;
import com.mesproject.constant.WorkOrdersStatus;
import com.mesproject.dto.MaterialOrderDto;
import com.mesproject.dto.OrderDto;
import com.mesproject.entity.*;
import com.mesproject.repository.WorkOrdersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class WorkOrdersService {

    public List<WorkOrders> createWorkOrders(WorkPlan workPlan){
       //즙
        List<WorkOrders> workOrdersList = new ArrayList<>();
        LocalDateTime temp = LocalDateTime.now();

        WorkOrders workOrders1 = new WorkOrders();
        workOrders1.setWorkPlan(workPlan);
        //세척
        workOrders1.setProcessType(ProcessType.CLEANING);
        workOrders1.setDuration(Duration.ofHours(2));
        workOrders1.setScheduledDate(temp);
        workOrders1.setWorkOrdersStatus(WorkOrdersStatus.PENDING);
        workOrdersList.add(workOrders1);

        //추출
        temp = temp.plusHours(2);

        WorkOrders workOrders2 = new WorkOrders();
        workOrders2.setProcessType(ProcessType.EXTRACTION);
        workOrders2.setDuration(Duration.ofHours(24));
        workOrders2.setScheduledDate(temp);
        workOrders2.setWorkOrdersStatus(WorkOrdersStatus.PENDING);
        workOrdersList.add(workOrders2);

        //여과
        temp = temp.plusHours(24);

        WorkOrders workOrders3 = new WorkOrders();
        workOrders3.setProcessType(ProcessType.FILTRATION);
        workOrders3.setDuration(Duration.ofHours(4));
        workOrders3.setScheduledDate(temp);
        workOrders3.setWorkOrdersStatus(WorkOrdersStatus.PENDING);
        workOrdersList.add(workOrders3);

        //살균
        temp = temp.plusHours(4);

        WorkOrders workOrders4 = new WorkOrders();
        workOrders4.setProcessType(ProcessType.STERILIZATION);
        workOrders4.setDuration(Duration.ofHours(2));
        workOrders4.setScheduledDate(temp);
        workOrders4.setWorkOrdersStatus(WorkOrdersStatus.PENDING);
        workOrdersList.add(workOrders4);

        //충진
        temp = temp.plusHours(2);

        WorkOrders workOrders5 = new WorkOrders();
        workOrders5.setProcessType(ProcessType.FILLING);
        workOrders5.setDuration(Duration.ofHours(8));
        workOrders5.setScheduledDate(temp);
        workOrders5.setWorkOrdersStatus(WorkOrdersStatus.PENDING);
        workOrdersList.add(workOrders5);

        //검사
        temp = temp.plusHours(8);

        WorkOrders workOrders6 = new WorkOrders();
        workOrders6.setProcessType(ProcessType.INSPECTION);
        workOrders6.setDuration(Duration.ofHours(2));
        workOrders6.setScheduledDate(temp);
        workOrders6.setWorkOrdersStatus(WorkOrdersStatus.PENDING);
        workOrdersList.add(workOrders6);

        //포장
        temp = temp.plusHours(2);

        WorkOrders workOrders7 = new WorkOrders();
        workOrders7.setProcessType(ProcessType.PACKAGING);
        workOrders7.setDuration(Duration.ofHours(1));
        workOrders7.setScheduledDate(temp);
        workOrders7.setWorkOrdersStatus(WorkOrdersStatus.PENDING);
        workOrdersList.add(workOrders7);

        return workOrdersList;
    }
    /*
    작업시작 메서드
     선공정 종료 이전 후공정 시작 불가능하므로, 종료되지 않은 선공정이 있는지 유효성 검사를 거친다.
     담당자, 작업 시작 일시  저장하여 작업시작 처리
     공정에 필요한 원자재가 자동 출고 처리
     (1.세척이면, 양배추/흑마늘 출고
     2. 충진이면, 벌꿀 출고
     3. 혼합이면, 석류/매실, 콜라겐 출고
     */

    /*
    작업종료 메서드
    작업 시작시점 기준으로 작업 소요시간이 지나기 이전 종료처리가 불가능하므로 유효성 검사를 거친다.
     유효성 검사 완료 후 해당 공정이 완료 처리된다.
     */
}
