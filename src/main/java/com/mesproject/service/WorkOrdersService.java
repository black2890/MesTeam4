package com.mesproject.service;

import com.mesproject.constant.ProcessType;
import com.mesproject.constant.WorkOrdersStatus;
import com.mesproject.dto.MaterialInOutDto;
import com.mesproject.dto.MaterialOrderDto;
import com.mesproject.dto.OrderDto;
import com.mesproject.dto.WorkOrdersDto;
import com.mesproject.entity.*;
import com.mesproject.repository.InventoryRepository;
import com.mesproject.repository.WorkOrdersRepository;
import com.mesproject.repository.WorkPlanRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class WorkOrdersService {

    private final WorkOrdersRepository workOrdersRepository;
    private final WorkPlanRepository   workPlanRepository;
    private final InventoryRepository inventoryRepository;
    private final MaterialInOutService materialInOutService;
    private final WorkPlanServicce workPlanService;

    public List<WorkOrders> createWorkOrders(WorkPlan workPlan){
        List<WorkOrders> workOrdersList = new ArrayList<>();
        LocalDateTime temp = workPlan.getStart();

        //즙
        if(workPlan.getProduct().getProductId()==1||workPlan.getProduct().getProductId()==2){

            //세척
            WorkOrders workOrders1 = new WorkOrders();
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
        }
        //스틱
        else if(workPlan.getProduct().getProductId()==3||workPlan.getProduct().getProductId()==4){
            //혼합
            WorkOrders workOrders1 = new WorkOrders();
            workOrders1.setProcessType(ProcessType.MIX);
            workOrders1.setDuration(Duration.ofHours(8));
            workOrders1.setScheduledDate(temp);
            workOrders1.setWorkOrdersStatus(WorkOrdersStatus.PENDING);
            workOrdersList.add(workOrders1);

            //살균
            temp = temp.plusHours(8);

            WorkOrders workOrders2 = new WorkOrders();
            workOrders2.setProcessType(ProcessType.FILTRATION);
            workOrders2.setDuration(Duration.ofHours(2));
            workOrders2.setScheduledDate(temp);
            workOrders2.setWorkOrdersStatus(WorkOrdersStatus.PENDING);
            workOrdersList.add(workOrders2);

            //충진
            temp = temp.plusHours(2);

            WorkOrders workOrders3 = new WorkOrders();
            workOrders3.setProcessType(ProcessType.FILLING);
            workOrders3.setDuration(Duration.ofHours(2));
            workOrders3.setScheduledDate(temp);
            workOrders3.setWorkOrdersStatus(WorkOrdersStatus.PENDING);
            workOrdersList.add(workOrders3);

            //냉각
            temp = temp.plusHours(2);

            WorkOrders workOrders4 = new WorkOrders();
            workOrders4.setProcessType(ProcessType.COOLING);
            workOrders4.setDuration(Duration.ofHours(8));
            workOrders4.setScheduledDate(temp);
            workOrders4.setWorkOrdersStatus(WorkOrdersStatus.PENDING);
            workOrdersList.add(workOrders4);

            //검사
            temp = temp.plusHours(8);

            WorkOrders workOrders5 = new WorkOrders();
            workOrders5.setProcessType(ProcessType.INSPECTION);
            workOrders5.setDuration(Duration.ofHours(1));
            workOrders5.setScheduledDate(temp);
            workOrders5.setWorkOrdersStatus(WorkOrdersStatus.PENDING);
            workOrdersList.add(workOrders5);

            //포장
            temp = temp.plusHours(1);

            WorkOrders workOrders6 = new WorkOrders();
            workOrders6.setProcessType(ProcessType.PACKAGING);
            workOrders6.setDuration(Duration.ofHours(1));
            workOrders6.setScheduledDate(temp);
            workOrders6.setWorkOrdersStatus(WorkOrdersStatus.PENDING);
            workOrdersList.add(workOrders6);

        }

        for(WorkOrders workOrders : workOrdersList){
            workOrders.setWorkPlan(workPlan);
        }

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

    public void start(WorkOrdersDto workOrdersDto){
        WorkOrders workOrders = workOrdersRepository.findById(workOrdersDto.getWorkOrderId())
                .orElseThrow(EntityNotFoundException::new);

        WorkPlan workPlan = workOrders.getWorkPlan();
        List<WorkOrders> workOrdersList = workPlan.getWorkOrders();

        // 현재 작업 공정의 인덱스 찾기
        int currentIndex = workOrdersList.indexOf(workOrders);
        if (currentIndex == -1) {
            throw new IllegalStateException("작업 공정이 작업 계획에 없습니다: " + workOrders.getWorkOrderId());
        }

        if (currentIndex == 0) {
         workPlan.setWorkPlanStatus(WorkOrdersStatus.INPROGRESS);
        }

        // 현재 공정 이전의 모든 공정들이 완료되었는지 확인
        for (int i = 0; i < currentIndex; i++) {
            WorkOrders previousOrder = workOrdersList.get(i);
            if (!previousOrder.isCompleted()) {
                throw new IllegalStateException("선공정이 완료되지 않았습니다: " + previousOrder.getWorkOrderId());
            }
        }

        // 작업 시작 처리
        workOrders.setStart(workOrdersDto.getStart());
        workOrders.setWorker(workOrdersDto.getWorker());
        workOrders.setWorkOrdersStatus(WorkOrdersStatus.INPROGRESS);

        // 공정에 필요한 원자재 출고 처리
        //포장지, box 도 출고해야 함( 생산량에 따라)
        switch (workOrders.getProcessType()) {
            case CLEANING:
            case FILLING:
            case MIX:
                materialInOutService.Out(workOrders.getWorkOrderId(),workOrdersDto.getStart(),workOrdersDto.getWorker());
                 break;
            case PACKAGING:
                materialInOutService.outpackaging(workPlan,workOrdersDto.getStart(),workOrdersDto.getWorker());
            default:
                break;

        }

    }


    /*


    작업종료 메서드
    작업 시작시점 기준으로 작업 소요시간이 지나기 이전 종료처리가 불가능하므로 유효성 검사를 거친다.
     유효성 검사 완료 후 해당 공정이 완료 처리된다.
     */

    public void end(WorkOrdersDto workOrdersDto){
        WorkOrders workOrders = workOrdersRepository.findById(workOrdersDto.getWorkOrderId())
                .orElseThrow(EntityNotFoundException::new);

        LocalDateTime start = workOrders.getStart();
        LocalDateTime end = workOrdersDto.getEnd();
        workOrders.setTempDuration();
        long duration = workOrders.getDuration().toHours(); // 소요시간 (시간 단위)

        // 유효성 검사: 작업 시작 기준 소요시간이 지났는지 확인
        if (start != null && workOrdersDto.getEnd() != null) {
            Duration time = Duration.between(start, workOrdersDto.getEnd());
            if (time.toHours() < duration) {
                throw new IllegalStateException("작업 시작 기준 소요시간이 지나지 않았습니다.");
            }
        } else {
            throw new IllegalStateException("작업 시작 시간 또는 종료 시간이 설정되지 않았습니다.");
        }


        workOrders.setEnd(workOrdersDto.getEnd());
        workOrders.setWorkOrdersStatus(WorkOrdersStatus.COMPLETED);

        WorkPlan workPlan = workOrders.getWorkPlan();
        List<WorkOrders> workOrdersList = workPlan.getWorkOrders();

            //마지막 공정이면 재고 데이터 생성
            WorkOrders FinalWorkOrder = workOrdersList.get(workOrdersList.size() - 1);
            if (Objects.equals(workOrders.getWorkOrderId(), FinalWorkOrder.getWorkOrderId())) {
                Inventory inventory = Inventory.createInventory(workPlan);
                inventoryRepository.save(inventory);
                workPlan.setWorkPlanStatus(WorkOrdersStatus.COMPLETED);
                workPlanService.ProductionCompleted(workPlan);
            }
        }
    public void startWorks(Long workOrderId){
        WorkOrders workOrders = workOrdersRepository.findById(workOrderId)
                .orElseThrow(EntityNotFoundException::new);

        WorkPlan workPlan = workOrders.getWorkPlan();
        List<WorkOrders> workOrdersList = workPlan.getWorkOrders();

        // 작업 시작 처리
        workOrders.setWorkOrdersStatus(WorkOrdersStatus.INPROGRESS);

        // 공정에 필요한 원자재 출고 처리
        //포장지, box 도 출고해야 함( 생산량에 따라)
        switch (workOrders.getProcessType()) {
            case CLEANING:
            case FILLING:
            case MIX:
                materialInOutService.Out(workOrders.getWorkOrderId(),null,null);
                break;
            default:
                break;

        }

    }


        public void endWorks(Long workOrderId){
            WorkOrders workOrders = workOrdersRepository.findById(workOrderId)
                    .orElseThrow(EntityNotFoundException::new);
            workOrders.setWorkOrdersStatus(WorkOrdersStatus.COMPLETED);

            WorkPlan workPlan = workOrders.getWorkPlan();
            List<WorkOrders> workOrdersList = workPlan.getWorkOrders();

            //마지막 공정이면 재고 데이터 생성
            WorkOrders FinalWorkOrder = workOrdersList.get(workOrdersList.size() - 1);
            if (Objects.equals(workOrders.getWorkOrderId(), FinalWorkOrder.getWorkOrderId())) {
                Inventory inventory = Inventory.createInventory(workPlan);
                inventoryRepository.save(inventory);
                workPlan.setWorkPlanStatus(WorkOrdersStatus.COMPLETED);
                workPlanService.ProductionCompleted(workPlan);
            }

        }


}
