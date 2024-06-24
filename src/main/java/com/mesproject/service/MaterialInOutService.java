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
import com.mesproject.dto.DataTableDto;
import com.mesproject.dto.MaterialInOutDto;
import com.mesproject.dto.MaterialOrdersDto;
import com.mesproject.entity.MaterialInOut;
import com.mesproject.entity.MaterialOrders;
import com.mesproject.repository.MaterialInOutRepository;
import com.mesproject.repository.MaterialOrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.thymeleaf.util.StringUtils;

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

     // 페이지 및
    public DataTableDto getMaterialInOutData(MultiValueMap<String, String> formData) {
        int draw = Integer.parseInt(formData.get("draw").get(0));
        int start = Integer.parseInt(formData.get("start").get(0));
        int length = Integer.parseInt(formData.get("length").get(0));

        System.out.println(start +"입니다.");
        System.out.println(length +"입니다.");

        // 페이지 설정
        Pageable pageable = PageRequest.of(start / length, length);

        // 검색 조건 가져오기
        String searchType = formData.getFirst("searchType");
        String searchValue = formData.getFirst("searchValue");

        System.out.println(searchType +"입니다.");
        System.out.println(searchValue +"입니다.");

        // 페이지네이션을 이용한 데이터 조회
        Page<MaterialInOutDto> materialInOutPage;

        if (searchType.equals("검색 조건") || StringUtils.isEmpty(searchValue)
                //임시 예외 사항 조건문 처리
            || searchType.equals("유통기한") || searchType.equals("상태") || searchType.equals("입고일") || searchType.equals("출고일")) {
            // 검색 조건이 없는 경우 전체 데이터 조회
            materialInOutPage = materialInOutRepository.findMaterialInOut(pageable);
        } else {
            // 검색 조건이 있는 경우 해당 조건으로 데이터 조회
            materialInOutPage = materialInOutRepository.findMaterialInOutBySearchOption(
                    searchType,
                    searchValue,
                    pageable
            );
        }

        System.out.println(materialInOutPage.getContent());

        // DataTableDto에 결과 저장
        DataTableDto dto = new DataTableDto();
        dto.setDraw(draw);
        dto.setRecordsFiltered((int) materialInOutPage.getTotalElements());
        dto.setRecordsTotal((int) materialInOutRepository.count());
        dto.setData(materialInOutPage.getContent());

        return dto;
    }
}


