package com.mesproject.repository;

import com.mesproject.entity.MaterialInOut;
import com.mesproject.dto.MaterialInOutDto;
import com.mesproject.dto.MaterialOrdersDto;
import com.mesproject.entity.MaterialInOut;
import com.mesproject.entity.MaterialOrders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.mesproject.entity.WorkPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MaterialInOutRepository extends JpaRepository<MaterialInOut, Long> {
   List<MaterialInOut> findByWorkOrders_WorkOrderId(Long id);
   Optional<MaterialInOut> findByMaterialOrders_MaterialOrderId(Long id);
   List<MaterialInOut> findAllByProduct_ProductIdOrderByStorageDateAsc(Long id);
   @Query("SELECT SUM(m.quantity) FROM MaterialInOut m WHERE m.product.productId = :productId AND m.materialInOutStatus = 'STORAGE'")
   Long sumQuantityByProductIdAndStatus(@Param("productId") Long productId);


    @Query("SELECT NEW com.mesproject.dto.MaterialInOutsDto(mi.inoutId, p.productName, wp.quantity, mi.expirationDate, mi.materialInOutStatus, mi.storageDate, mi.storageWorker, mi.retrievalDate, mi.retrievalWorker) " +
                "FROM MaterialInOut mi " +
                "LEFT JOIN mi.workOrders wo " +
                "LEFT JOIN wo.workPlan wp " +
                "LEFT JOIN wp.product p")
    Page<MaterialInOutDto> findMaterialInOut(Pageable pageable);

    @Query("SELECT NEW com.mesproject.dto.MaterialInOutsDto(mi.inoutId, p.productName, wp.quantity, mi.expirationDate, mi.materialInOutStatus, mi.storageDate, mi.storageWorker, mi.retrievalDate, mi.retrievalWorker) " +
            "FROM MaterialInOut mi " +
            "LEFT JOIN mi.workOrders wo " +
            "LEFT JOIN wo.workPlan wp " +
            "LEFT JOIN wp.product p " +
            "WHERE (:searchType = '제품명' AND p.productName LIKE %:searchValue%)")
    Page<MaterialInOutDto> findMaterialInOutBySearchOption(@Param("searchType") String searchType,
                                                             @Param("searchValue") String searchValue,
                                                             Pageable pageable);
                                                             
   @Query("SELECT SUM(m.quantity) FROM MaterialInOut m WHERE m.product.productId = :productId AND m.materialInOutStatus = 'PENDINGSTORAGE'")
   Long sumQuantityByProductIdAndPendingStatus(@Param("productId") Long productId);

   @Query("SELECT m.product.productName, SUM(m.quantity) FROM MaterialInOut m WHERE m.materialInOutStatus = 'STORAGE' GROUP BY m.product.productName")
   List<Object[]> findMaterialTotalStockByProductNameAndStatus();



   @Query("SELECT m FROM MaterialInOut m WHERE m.product.productId = :productId AND m.materialInOutStatus = 'STORAGE'" +
           "ORDER BY m.storageDate ASC")
   List<MaterialInOut> findByProductIdAndStatusOrderByStorageDateAsc(@Param("productId") Long productId);



}
