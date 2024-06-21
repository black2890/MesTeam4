package com.mesproject.repository;

import com.mesproject.dto.MaterialInOutDto;
import com.mesproject.dto.MaterialOrdersDto;
import com.mesproject.entity.MaterialInOut;
import com.mesproject.entity.MaterialOrders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MaterialInOutRepository extends JpaRepository<MaterialInOut, Long> {

    @Query("SELECT NEW com.mesproject.dto.MaterialInOutDto(mi.inoutId, p.productName, wp.quantity, mi.expirationDate, mi.materialInOutStatus, mi.storageDate, mi.storageWorker, mi.retrievalDate, mi.retrievalWorker) " +
            "FROM MaterialInOut mi " +
            "LEFT JOIN mi.workOrders wo " +
            "LEFT JOIN wo.workPlan wp " +
            "LEFT JOIN wp.product p")
    Page<MaterialInOutDto> findMaterialInOut(Pageable pageable);

    @Query("SELECT NEW com.mesproject.dto.MaterialInOutDto(mi.inoutId, p.productName, wp.quantity, mi.expirationDate, mi.materialInOutStatus, mi.storageDate, mi.storageWorker, mi.retrievalDate, mi.retrievalWorker) " +
            "FROM MaterialInOut mi " +
            "LEFT JOIN mi.workOrders wo " +
            "LEFT JOIN wo.workPlan wp " +
            "LEFT JOIN wp.product p " +
            "WHERE (:searchType = '제품명' AND p.productName LIKE %:searchValue%)")
    Page<MaterialInOutDto> findMaterialInOutBySearchOption(@Param("searchType") String searchType,
                                                             @Param("searchValue") String searchValue,
                                                             Pageable pageable);
}

