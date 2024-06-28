package com.mesproject.service;

import com.mesproject.constant.ProcessType;
import com.mesproject.dto.MaterialOrderDto;
import com.mesproject.dto.OrderDto;
import com.mesproject.entity.*;
import com.mesproject.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.jdbc.Work;
import org.hibernate.query.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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

    private static WorkPlanRepository staticWorkPlanRepository;
    private OrdersPlanRepository ordersPlanRepository;
    private OrdersMaterialsRepository ordersMaterialsRepository;

    @Autowired
    public void setWorkPlanRepository(WorkPlanRepository workPlanRepository) {
        OrderService.staticWorkPlanRepository = workPlanRepository;
    }

    /*납품일 받았을 때, 생산계획 비어있다고 생각하고 넣는 메서드
    만약, 생산계획 차있으면 그 자리는 피해서 넣어야함.
    스틱은 있으면 건너뛰고 하루에 할당하면 되는데
    즙은 하루만 비어있으면 그 때는 생산 못하고 이틀 비어있는 곳에 넣어야 함.
     */
    public Long order(OrderDto orderDto){
        //불량률 고려 3% 추가 생산
        long initialQuantity = orderDto.getQuantity();
        orderDto.setQuantity((long) Math.ceil(orderDto.getQuantity() * 1.03));

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

        //  LocalDateTime productionDate = getProductionDate(orderDto, product, result.numberOfProduction());
        LocalDateTime productionDate =LocalDateTime.now().toLocalDate().plusDays(1).atStartOfDay();


        Optional<WorkPlan> optionalWorkPlan = workPlanRepository.findFirstByProduct_ProductIdAndStartGreaterThanEqualOrderByStartDescWorkPlanIdDesc(product.getProductId(), productionDate);



        //maxquantity 설정, 생산횟수 계산
        Result result = getResult(orderDto, product);

        boolean isIntegrated = false;
        WorkPlan tempWorkPlan = null;
        if (optionalWorkPlan.isPresent()) {
            tempWorkPlan = optionalWorkPlan.get();
            // tempWorkPlan을 이용한 로직 작성
        } else {
            // tempWorkPlan이 존재하지 않을 경우의 로직 작성
        }


        int totalQuantity =0;
        int tempQuantity =0;
        if(tempWorkPlan!=null && tempWorkPlan.getQuantity() != result.maxQuantity()){
            isIntegrated= true;
            totalQuantity= (int) (orderDto.getQuantity() + tempWorkPlan.getQuantity());
            tempQuantity = Math.toIntExact(orderDto.getQuantity());
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
        //현재방식의 문제점 : workplan 3 을 333으로 저장해버림. 실제 생산량과 괴리 있음.
        List<OrdersMaterials> ordersMaterialsList = new ArrayList<>();
        List<OrdersPlan> ordersPlanList = new ArrayList<>();
        //반복문 들어가기전에 count= 1 로 설정
        int count =1;


        for(int i = 0; i< result.numberOfProduction(); i++){
            //작업계획 생성
        /*
        productid가 1,2이면 2일
        productid가 3,4이면 1일
         */
            WorkPlan workPlan = null;
            if(!isIntegrated){

                if(count== result.numberOfProduction()) {
                    //처음에 count 가 1, numberofProduction 이 1
                    if (orderDto.getQuantity() % result.maxQuantity() == 0) {
                        workPlan = WorkPlan.createWorkPlan(product, (long) result.maxQuantity());
                    } else {
                        workPlan = WorkPlan.createWorkPlan(product, orderDto.getQuantity() % result.maxQuantity());
                    }
                }
                else{
                    workPlan = WorkPlan.createWorkPlan(product, (long) result.maxQuantity());

                }

            }else{
                if(count== 1) {


                    if(count!= result.numberOfProduction()){
                        tempWorkPlan.setQuantity((long) result.maxQuantity());
                    }else{
                        tempWorkPlan.setQuantity((long) totalQuantity);
                    }

                }else if(result.numberOfProduction() != count) {
                    workPlan = WorkPlan.createWorkPlan(product, (long) result.maxQuantity());
                }else {
                    if(totalQuantity % result.maxQuantity() ==0){
                        workPlan = WorkPlan.createWorkPlan(product, (long) result.maxQuantity());
                    }else{
                        workPlan = WorkPlan.createWorkPlan(product, (long) (totalQuantity % result.maxQuantity()));
                    }
                }

            }
            count++;
            if(isIntegrated && count==2){
                    OrdersPlan ordersPlan = ordersPlanRepository.findFirstByWorkPlan_WorkPlanIdOrderByOrders_OrderIdAsc(
                            tempWorkPlan.getWorkPlanId()).orElseThrow(EntityNotFoundException::new);
                    Long orderId = ordersPlan.getOrders().getOrderId();
                    List<OrdersMaterials> ordersMaterials = ordersMaterialsRepository.findByOrders_OrderId(orderId);
                    List<MaterialOrders> materialOrders = new ArrayList<>();;

                    for(OrdersMaterials ordersMaterial: ordersMaterials){
                        materialOrders.add(ordersMaterial.getMaterialOrders());
                    }
                    for(MaterialOrders materialOrder: materialOrders){
                        //수주-발주 엔티티 생성 : 수주코드 저장은 createorder 에서
                        OrdersMaterials ordersMaterial = OrdersMaterials.createOrdersMaterials(materialOrder);
                        ordersMaterialsList.add(ordersMaterial);
                    }

                //수주-작업계획 엔티티 생성 :  수주코드 저장은 createorder 에서
                OrdersPlan tempOrdersPlan = OrdersPlan.createOrdersPlan(tempWorkPlan);
                workPlanRepository.save(tempWorkPlan);
                ordersPlanList.add(tempOrdersPlan);



            }
          else{
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

                if(orderDto.getProductId()==1||orderDto.getProductId()==2 && workPlan != null){
                    Long checkProductId ;
                    if(orderDto.getProductId() ==1){
                        checkProductId= 2L;
                    }else{
                        checkProductId= 1L;
                    }


                    List<WorkPlan> tempWorkPlanList = workPlanRepository.findByProduct_ProductIdAndEnd(checkProductId, productionDate.minusDays(1).toLocalDate());
                    if(!tempWorkPlanList.isEmpty()){
                        while(tempWorkPlanList.size()>=2){
                            productionDate = productionDate.plusDays(1);
                            tempWorkPlanList = workPlanRepository.findByProduct_ProductIdAndEnd(checkProductId, productionDate.minusDays(1).toLocalDate());
                        }

                    }
                    //추출기 사용할 수 있는지 확인
                    List<WorkPlan> tempWorkPlanList2 = workPlanRepository.findByProducts_ProductIdAndEnd(1L, 2L, productionDate.toLocalDate());
                    if(!tempWorkPlanList2.isEmpty()){
                        while(tempWorkPlanList2.size()>=2){
                            productionDate = productionDate.plusDays(1);
                            tempWorkPlanList2 = workPlanRepository.findByProducts_ProductIdAndEnd(1L, 2L, productionDate.toLocalDate());
                        }

                    }






                    List<WorkPlan> workPlanList = workPlanRepository.findByProductIdAndStartDateAfter(1L,2L,productionDate);

                    if(workPlanList.isEmpty()){
                        workPlan.setStart(productionDate);
                        workPlan.setEnd(productionDate.plusHours(44));

                    }
                    else {
                        boolean isFirst = true;  // 1일차에 새 작업계획 편성 가능 여부
                        boolean isSecond = true;
                        boolean existFirst = false; //1일차 기존 작업계획 유무
                        boolean existSecond = false;

                        for(WorkPlan temp:workPlanList){
                            //1일차 조회
                            if(temp.getStart().toLocalDate().equals(productionDate.toLocalDate()) && !existFirst){
                                existFirst = true;
                                continue;
                            }else if(temp.getStart().toLocalDate().equals(productionDate.toLocalDate()) && existFirst ){
                                isFirst = false;
                            }else{
                                isFirst = true;
                            }

                            if(!isFirst){
                                productionDate = productionDate.plusDays(2);

                                tempWorkPlanList = workPlanRepository.findByProduct_ProductIdAndEnd(checkProductId, productionDate.minusDays(1).toLocalDate());
                                if(!tempWorkPlanList.isEmpty()){
                                    while(tempWorkPlanList.size()>=2){
                                        productionDate = productionDate.plusDays(1);
                                        tempWorkPlanList = workPlanRepository.findByProduct_ProductIdAndEnd(checkProductId, productionDate.minusDays(1).toLocalDate());
                                    }

                                }

                                //추출기 사용할 수 있는지 확인
                               tempWorkPlanList2 = workPlanRepository.findByProducts_ProductIdAndEnd(1L, 2L, productionDate.toLocalDate());
                                if(!tempWorkPlanList2.isEmpty()){
                                    while(tempWorkPlanList2.size()>=2){
                                        productionDate = productionDate.plusDays(1);
                                        tempWorkPlanList2 =workPlanRepository.findByProducts_ProductIdAndEnd(1L, 2L, productionDate.toLocalDate());
                                    }

                                }
                                isFirst = true;  // 1일차에 새 작업계획 편성 가능 여부
                                isSecond = true;
                                existFirst = false; //1일차 기존 작업계획 유무
                                existSecond = false;
                                continue;
                            }
                            productionDate = productionDate.plusDays(1);

                            //2일차 조회
                            if(temp.getStart().toLocalDate().equals(productionDate.toLocalDate()) && !existSecond){
                                existSecond = true;
                                continue;
                            }else if(temp.getStart().toLocalDate().equals(productionDate.toLocalDate()) && existSecond){
                                isSecond = false;
                            }else{
                                isSecond = true;
                            }

                            //조건 모두 만족하면 생산계획 편성, 그렇지 않으면 productiondate++
                            if(isFirst && isSecond){
                                productionDate = productionDate.minusDays(1);
                                workPlan.setStart(productionDate);
                                workPlan.setEnd(productionDate.plusHours(44));

                                break;
                            }else{
                                productionDate = productionDate.plusDays(1);
                                tempWorkPlanList = workPlanRepository.findByProduct_ProductIdAndEnd(checkProductId, productionDate.minusDays(1).toLocalDate());
                                if(!tempWorkPlanList.isEmpty()){
                                    while(tempWorkPlanList.size()>=2){
                                        productionDate = productionDate.plusDays(1);
                                        tempWorkPlanList = workPlanRepository.findByProduct_ProductIdAndEnd(checkProductId, productionDate.minusDays(1).toLocalDate());
                                    }

                                }
                                //추출기 사용할 수 있는지 확인
                                tempWorkPlanList2 = workPlanRepository.findByProducts_ProductIdAndEnd(1L, 2L, productionDate.toLocalDate());
                                if(!tempWorkPlanList2.isEmpty()){
                                    while(tempWorkPlanList2.size()>=2){
                                        productionDate = productionDate.plusDays(1);
                                        tempWorkPlanList2 = workPlanRepository.findByProducts_ProductIdAndEnd(1L, 2L, productionDate.toLocalDate());
                                    }

                                }

                                isFirst = true;  // 1일차에 새 작업계획 편성 가능 여부
                                isSecond = true;
                                existFirst = false; //1일차 기존 작업계획 유무
                                existSecond = false;
                            }

                        }

                        workPlan.setStart(productionDate);
                        workPlan.setEnd(productionDate.plusHours(44));
                        //  productionDate = productionDate.plusDays(1);
                    }

                    //리스트에 데이터 받아와서 반복문 돌면서 겹치는 날짜 있는지 확인

                }

                else if(orderDto.getProductId()==3||orderDto.getProductId()==4 && workPlan != null){

                    List<WorkPlan> workPlanList = workPlanRepository.findByProductIdAndStartDateAfter(3L,4L,productionDate);

                    if(workPlanList.isEmpty()){
                        workPlan.setStart(productionDate);

                    }
                    else {

                        for(WorkPlan temp:workPlanList){
                            if(temp.getStart().toLocalDate().equals(productionDate.toLocalDate())){
                                productionDate = productionDate.plusDays(1);

                            }else{
                                workPlan.setStart(productionDate);
                                break;
                            }

                        }
                    }

                    //리스트에 데이터 받아와서 반복문 돌면서 겹치는 날짜 있는지 확인
                    workPlan.setStart(productionDate);
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






        }
        orderDto.setQuantity((long) initialQuantity);
        //수주 생성
        Orders order = Orders.createOrder(ordersMaterialsList,ordersPlanList ,product,vendor,orderDto);
        ordersRepository.save(order);


        return order.getOrderId();
    }

    private static Result getResult(OrderDto orderDto, Product product) {
        // 생산횟수 계산
        int numberOfProduction=0;
        int maxQuantity=0;
        boolean isIntegratedGlobal = false;



        LocalDateTime productionDate =LocalDateTime.now().toLocalDate().atStartOfDay().plusDays(1);



        Optional<WorkPlan> optionalWorkPlan = staticWorkPlanRepository.findFirstByProduct_ProductIdAndStartGreaterThanEqualOrderByStartDescWorkPlanIdDesc(
                product.getProductId(), productionDate
        );
        WorkPlan tempWorkPlan = null;


        if (optionalWorkPlan.isPresent()) {
            tempWorkPlan = optionalWorkPlan.get();
            // tempWorkPlan을 이용한 로직 작성
        } else {
            // tempWorkPlan이 존재하지 않을 경우의 로직 작성
        }


        if(product.getProductId()==1 || product.getProductId()==2){
            maxQuantity=333;
//            if(orderDto.getQuantity() ==1000){
//                orderDto.setQuantity(999L);
//            }

        }else if(product.getProductId()==3|| product.getProductId()==4){
            maxQuantity=160;

        }
        if(orderDto.getQuantity()%maxQuantity ==0){
            numberOfProduction = (int)(orderDto.getQuantity()/maxQuantity);
        }else {
            numberOfProduction = (int)(orderDto.getQuantity()/maxQuantity)+1;
        }
        //일단 스틱만 capa 고려 통합
        if(tempWorkPlan!=null && tempWorkPlan.getQuantity()!=maxQuantity){
            isIntegratedGlobal = true;

            if((tempWorkPlan.getQuantity()+orderDto.getQuantity())%maxQuantity ==0){
                numberOfProduction = (int)((tempWorkPlan.getQuantity()+orderDto.getQuantity())/maxQuantity);
            }else {
                numberOfProduction = (int)((tempWorkPlan.getQuantity()+orderDto.getQuantity())/maxQuantity)+1;
            }

        }

        Result result = new Result(numberOfProduction, maxQuantity,isIntegratedGlobal);
        return result;
    }

    @Autowired
    public void setOrdersPlanRepository(OrdersPlanRepository ordersPlanRepository) {
        this.ordersPlanRepository = ordersPlanRepository;
    }

    @Autowired
    public void setOrdersMaterialsRepository(OrdersMaterialsRepository ordersMaterialsRepository) {
        this.ordersMaterialsRepository = ordersMaterialsRepository;
    }



    private record Result(int numberOfProduction, int maxQuantity, boolean isIntegratedGlobal) {
    }

    private static LocalDateTime getProductionDate(OrderDto orderDto, Product product, int numberOfProduction) {
        //생산 시작일시 계산
        //납품일에 맞춰서 생산일시 계산하는 로직필요
        int productionPeriod = 0;
        LocalDateTime deliveryDate = orderDto.getDeliveryDate();
        LocalDateTime productionDate = null;

        if(product.getProductId()==1 || product.getProductId()==2){
            productionPeriod=2;
            productionDate = deliveryDate.minusDays((long) productionPeriod * numberOfProduction /2 + 2);
        }else if(product.getProductId()==3|| product.getProductId()==4){
            productionPeriod=1;
            productionDate = deliveryDate.minusDays(productionPeriod* numberOfProduction + 2);
        }
        return productionDate;
    }

    //capa 통합 여부 고려해야 함
    public LocalDate calculateEstimatedDate(Product product, int quantity){
        if(product.getProductId()==1||product.getProductId()==2){

            LocalDate productionDate = LocalDate.now().plusDays(1);
            Long checkProductId ;
            if(product.getProductId() ==1){
                checkProductId= 2L;
            }else{
                checkProductId= 1L;
            }


            List<WorkPlan> tempWorkPlanList = workPlanRepository.findByProduct_ProductIdAndEnd(checkProductId, productionDate.minusDays(1));
            if(!tempWorkPlanList.isEmpty()){
                while(tempWorkPlanList.size()>=2){
                    productionDate = productionDate.plusDays(1);
                    tempWorkPlanList = workPlanRepository.findByProduct_ProductIdAndEnd(checkProductId, productionDate.minusDays(1));
                }

            }
            //추출기 사용할 수 있는지 확인
            List<WorkPlan>  tempWorkPlanList2 = workPlanRepository.findByProducts_ProductIdAndEnd(1L, 2L, productionDate);
            if(!tempWorkPlanList2.isEmpty()){
                while(tempWorkPlanList2.size()>=2){
                    productionDate = productionDate.plusDays(1);
                    tempWorkPlanList2 =workPlanRepository.findByProducts_ProductIdAndEnd(1L, 2L, productionDate);
                }

            }

            List<WorkPlan> workPlanList = workPlanRepository.findByProductIdAndStartDateAfter(
                    1L,2L,LocalDateTime.now().toLocalDate().atStartOfDay().plusDays(1));

            OrderDto orderDto = new OrderDto();
            orderDto.setQuantity((long) quantity);
            Result result = getResult(orderDto, product);

            //capa 통합 가능하면 생산횟수 -1
            int numberOfProduction=0;
            if(result.isIntegratedGlobal()){
                numberOfProduction = result.numberOfProduction()-1;
            }else{
                numberOfProduction = result.numberOfProduction();
            }



            int count =0;
            if(workPlanList.isEmpty()){
                //생산완료 후 3일 뒤 배송완료
                count=numberOfProduction;
                return productionDate.plusDays(count+3);

            }

            boolean isFirst = true;  // 1일차에 새 작업계획 편성 가능 여부
            boolean isSecond = true;
            boolean existFirst = false; //1일차 기존 작업계획 유무
            boolean existSecond = false;


            for(WorkPlan temp:workPlanList){

                //1일차 조회
                if(temp.getStart().toLocalDate().equals(productionDate) && !existFirst){
                    existFirst = true;
                    continue;
                }else if(temp.getStart().toLocalDate().equals(productionDate) && existFirst){
                    isFirst = false;
                }else{
                    isFirst = true;
                }

                if(!isFirst){
                    productionDate = productionDate.plusDays(2);

                    tempWorkPlanList = workPlanRepository.findByProduct_ProductIdAndEnd(checkProductId, productionDate.minusDays(1));
                    if(!tempWorkPlanList.isEmpty()){
                        while(tempWorkPlanList.size()>=2){
                            productionDate = productionDate.plusDays(1);
                            tempWorkPlanList = workPlanRepository.findByProduct_ProductIdAndEnd(checkProductId, productionDate.minusDays(1));
                        }

                    }

                    //추출기 사용할 수 있는지 확인
                    tempWorkPlanList2 = workPlanRepository.findByProducts_ProductIdAndEnd(1L, 2L, productionDate);
                    if(!tempWorkPlanList2.isEmpty()){
                        while(tempWorkPlanList2.size()>=2){
                            productionDate = productionDate.plusDays(1);
                            tempWorkPlanList2 =workPlanRepository.findByProducts_ProductIdAndEnd(1L, 2L, productionDate);
                        }

                    }
                    isFirst = true;  // 1일차에 새 작업계획 편성 가능 여부
                    isSecond = true;
                    existFirst = false; //1일차 기존 작업계획 유무
                    existSecond = false;
                    productionDate = productionDate.plusDays(2);
                    continue;
                }
                productionDate = productionDate.plusDays(1);

                //2일차 조회
                if(temp.getStart().toLocalDate().equals(productionDate) && !existSecond){
                    existSecond = true;
                    continue;
                }else if(temp.getStart().toLocalDate().equals(productionDate) && existSecond){
                    isSecond = false;
                }else{
                    isSecond = true;
                }

                //조건 모두 만족하면 생산계획 편성, 그렇지 않으면 productiondate++
                if(isFirst && isSecond){
                    count++;
                    if(count==numberOfProduction){
                        //생산완료후 3일뒤 배송완료
                        return productionDate.plusDays(3);
                    }
                    productionDate = productionDate.minusDays(1);


                }else{
                    productionDate = productionDate.plusDays(1);
                    tempWorkPlanList = workPlanRepository.findByProduct_ProductIdAndEnd(checkProductId, productionDate.minusDays(1));
                    if(!tempWorkPlanList.isEmpty()){
                        while(tempWorkPlanList.size()>=2){
                            productionDate = productionDate.plusDays(1);
                            tempWorkPlanList = workPlanRepository.findByProduct_ProductIdAndEnd(checkProductId, productionDate.minusDays(1));
                        }

                    }
                    //추출기 사용할 수 있는지 확인
                    tempWorkPlanList2 = workPlanRepository.findByProducts_ProductIdAndEnd(1L, 2L, productionDate);
                    if(!tempWorkPlanList2.isEmpty()){
                        while(tempWorkPlanList2.size()>=2){
                            productionDate = productionDate.plusDays(1);
                            tempWorkPlanList2 =workPlanRepository.findByProducts_ProductIdAndEnd(1L, 2L, productionDate);
                        }

                    }
                }

                isFirst = true;  // 1일차에 새 작업계획 편성 가능 여부
                isSecond = true;
                existFirst = false; //1일차 기존 작업계획 유무
                existSecond = false;




            }

            if(count!=numberOfProduction){
                return productionDate.plusDays(numberOfProduction-count+2);
            }


        } else if(product.getProductId()==3||product.getProductId()==4){

            List<WorkPlan> workPlanList = workPlanRepository.findByProductIdAndStartDateAfter(
                    3L,4L,LocalDateTime.now().toLocalDate().atStartOfDay().plusDays(1));

            OrderDto orderDto = new OrderDto();
            orderDto.setQuantity((long) quantity);
            Result result = getResult(orderDto, product);

            int numberOfProduction=0;
            if(result.isIntegratedGlobal()){
                numberOfProduction = result.numberOfProduction()-1;
            }else{
                numberOfProduction = result.numberOfProduction();
            }

            LocalDate productionDate = LocalDate.now().plusDays(1);

            int count =0;
            if(workPlanList.isEmpty()){
                //생산완료 후 3일 뒤 배송완료
                count=numberOfProduction;
                return productionDate.plusDays(count+3);

            }

            for(WorkPlan temp:workPlanList){
                    if(temp.getStart().toLocalDate().equals(productionDate)){
                        productionDate = productionDate.plusDays(1);

                    }else{
                        count++;
                        if(count==numberOfProduction){
                            //생산완료 후 3일 뒤 배송완료
                            return productionDate.plusDays(3);
                        }
                        productionDate = productionDate.plusDays(1);
                    }


            }

            if(count!=numberOfProduction){
                //이미 productiondate +1 된 상태로 넘어옴
                return productionDate.plusDays(numberOfProduction-count+2);
            }


        }
    return null;
        
    }
    public List<OrderDto> parseExcelFile(MultipartFile file) throws Exception {
        List<OrderDto> orders = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // Skip header row
                }


                OrderDto orderDto = new OrderDto();
                orderDto.setProductId((long) row.getCell(0).getNumericCellValue());
                orderDto.setVendorId((long) row.getCell(1).getNumericCellValue());
                orderDto.setQuantity((long) row.getCell(2).getNumericCellValue());
      //          row.getCell(3).getStringCellValue();
//                Date date = sdf.parse(row.getCell(3).getStringCellValue());
                Date date = row.getCell(3).getDateCellValue();
                LocalDateTime localDateTime = new java.sql.Timestamp(date.getTime()).toLocalDateTime();


//
//                orderDto.setDeliveryDate(parseDate(row.getCell(3).getDateCellValue()));
    //            orderDto.setDeliveryDate(LocalDateTime.parse(sdf.format(row.getCell(3).getDateCellValue())));

                orderDto.setDeliveryDate(localDateTime);
                orderDto.setDeliveryAddress(row.getCell(4).getStringCellValue());

                orders.add(orderDto);
            }
        }
        return orders;
    }

    public LocalDate parseDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            return LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Invalid date format. Please use yyyy-MM-dd format.");
        }
    }

// 엑셀 파싱 코드에서




        LocalDateTime productionDate =LocalDateTime.now().toLocalDate().atStartOfDay().plusDays(1);

        Pageable pageable = PageRequest.of(0, 1); // 첫 번째 페이지의 첫 번째 결과만 가져옴

        Optional<WorkPlan> optionalWorkPlan = staticWorkPlanRepository.findFirstByProduct_ProductIdAndStartGreaterThanEqualOrderByStartDescWorkPlanIdDesc(
                product.getProductId(), productionDate
        );
        WorkPlan tempWorkPlan = null;


        if (optionalWorkPlan.isPresent()) {
            tempWorkPlan = optionalWorkPlan.get();
            // tempWorkPlan을 이용한 로직 작성
        } else {
            // tempWorkPlan이 존재하지 않을 경우의 로직 작성
        }


        if(product.getProductId()==1 || product.getProductId()==2){
            maxQuantity=333;
//            if(orderDto.getQuantity() ==1000){
//                orderDto.setQuantity(999L);
//            }

        }else if(product.getProductId()==3|| product.getProductId()==4){
            maxQuantity=160;

        }
        if(orderDto.getQuantity()%maxQuantity ==0){
            numberOfProduction = (int)(orderDto.getQuantity()/maxQuantity);
        }else {
            numberOfProduction = (int)(orderDto.getQuantity()/maxQuantity)+1;
        }
        //일단 스틱만 capa 고려 통합
        if(tempWorkPlan!=null && tempWorkPlan.getQuantity()!=maxQuantity){

            if((tempWorkPlan.getQuantity()+orderDto.getQuantity())%maxQuantity ==0){
                numberOfProduction = (int)((tempWorkPlan.getQuantity()+orderDto.getQuantity())/maxQuantity);
            }else {
                numberOfProduction = (int)((tempWorkPlan.getQuantity()+orderDto.getQuantity())/maxQuantity)+1;
            }

        }

        Result result = new Result(numberOfProduction, maxQuantity);
        return result;
    }

    @Autowired
    public void setOrdersPlanRepository(OrdersPlanRepository ordersPlanRepository) {
        this.ordersPlanRepository = ordersPlanRepository;
    }

    @Autowired
    public void setOrdersMaterialsRepository(OrdersMaterialsRepository ordersMaterialsRepository) {
        this.ordersMaterialsRepository = ordersMaterialsRepository;
    }

    private record Result(int numberOfProduction, int maxQuantity) {
    }

    private static LocalDateTime getProductionDate(OrderDto orderDto, Product product, int numberOfProduction) {
        //생산 시작일시 계산
        //납품일에 맞춰서 생산일시 계산하는 로직필요
        int productionPeriod = 0;
        LocalDateTime deliveryDate = orderDto.getDeliveryDate();
        LocalDateTime productionDate = null;

        if(product.getProductId()==1 || product.getProductId()==2){
            productionPeriod=2;
            productionDate = deliveryDate.minusDays((long) productionPeriod * numberOfProduction /2 + 2);
        }else if(product.getProductId()==3|| product.getProductId()==4){
            productionPeriod=1;
            productionDate = deliveryDate.minusDays(productionPeriod* numberOfProduction + 2);
        }
        return productionDate;
    }

    public LocalDate calculateEstimatedDate(Product product, int quantity){
        if(product.getProductId()==1||product.getProductId()==2){

            List<WorkPlan> workPlanList = workPlanRepository.findByProductIdAndStartDateAfter(
                    1L,2L,LocalDateTime.now().toLocalDate().atStartOfDay().plusDays(1));

            OrderDto orderDto = new OrderDto();
            orderDto.setQuantity((long) quantity);
            Result result = getResult(orderDto, product);

            int numberOfProduction = result.numberOfProduction();
            LocalDate productionDate = LocalDate.now().plusDays(1);

            int count =0;
            if(workPlanList.isEmpty()){
                count=numberOfProduction;
                return LocalDate.now().plusDays(count+3);

            }

            boolean isFirst = true;  // 1일차에 새 작업계획 편성 가능 여부
            boolean isSecond = true;
            boolean existFirst = false; //1일차 기존 작업계획 유무
            boolean existSecond = false;


            for(WorkPlan temp:workPlanList){

                //1일차 조회
                if(temp.getStart().toLocalDate().equals(productionDate) && !existFirst){
                    existFirst = true;
                    continue;
                }else if(temp.getStart().toLocalDate().equals(productionDate) && existFirst){
                    isFirst = false;
                }else{
                    isFirst = true;
                }

                if(!isFirst){
                    isFirst = true;  // 1일차에 새 작업계획 편성 가능 여부
                    isSecond = true;
                    existFirst = false; //1일차 기존 작업계획 유무
                    existSecond = false;
                    productionDate = productionDate.plusDays(2);
                    continue;
                }
                productionDate = productionDate.plusDays(1);

                //2일차 조회
                if(temp.getStart().toLocalDate().equals(productionDate) && !existSecond){
                    existSecond = true;
                    continue;
                }else if(temp.getStart().toLocalDate().equals(productionDate) && existSecond){
                    isSecond = false;
                }else{
                    isSecond = true;
                }

                //조건 모두 만족하면 생산계획 편성, 그렇지 않으면 productiondate++
                if(isFirst && isSecond){
                    productionDate = productionDate.minusDays(1);
                    count++;

                }else{
                    productionDate = productionDate.plusDays(1);
                }

                isFirst = true;  // 1일차에 새 작업계획 편성 가능 여부
                isSecond = true;
                existFirst = false; //1일차 기존 작업계획 유무
                existSecond = false;

                if(count!=numberOfProduction){
                    return productionDate.plusDays(numberOfProduction-count+2);
                }


            }

            if(count!=numberOfProduction){
                return productionDate.plusDays(numberOfProduction-count+2);
            }


        } else if(product.getProductId()==3||product.getProductId()==4){

            List<WorkPlan> workPlanList = workPlanRepository.findByProductIdAndStartDateAfter(
                    3L,4L,LocalDateTime.now().toLocalDate().atStartOfDay().plusDays(1));

            OrderDto orderDto = new OrderDto();
            orderDto.setQuantity((long) quantity);
            Result result = getResult(orderDto, product);

            int numberOfProduction = result.numberOfProduction();
            LocalDate productionDate = LocalDate.now().plusDays(1);

            int count =0;
            if(workPlanList.isEmpty()){
                count=numberOfProduction;
                return LocalDate.now().plusDays(count+2);

            }

            for(WorkPlan temp:workPlanList){
                    if(temp.getStart().toLocalDate().equals(productionDate)){
                        productionDate = productionDate.plusDays(1);

                    }else{
                        count++;
                        productionDate = productionDate.plusDays(1);
                    }
                    if(count==numberOfProduction){
                        return productionDate.plusDays(2);
                    }

            }

            if(count!=numberOfProduction){
                return productionDate.plusDays(numberOfProduction-count+2);
            }


        }
    return null;
        
    }

    public List<Map<String, Object>> getRelatedOrdersByWorkPlanId(Long workPlanId) {
        return workPlanRepository.findOrdersByWorkPlanId(workPlanId);
    }

}
