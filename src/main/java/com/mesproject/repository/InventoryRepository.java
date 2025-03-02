package com.mesproject.repository;

import com.mesproject.entity.Inventory;
import com.mesproject.entity.OrdersPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {


    @Query("SELECT i FROM Inventory i JOIN i.product p WHERE p.productName LIKE %:productName%")
    Page<Inventory> findAllByProductNameContaining(String productName, Pageable pageable);

    Page<Inventory> findAllByProduct_ProductNameContaining(String searchValue, Pageable pageable);

    @Query("SELECT i.product.productName, SUM(i.quantity) FROM Inventory i WHERE i.inventoryStatus = 'STORAGECOMPLETED' GROUP BY i.product.productName")
    List<Object[]> findTotalStockByProductNameAndStatus();



    Inventory findByWorkPlan_WorkPlanId(Long workPlanId);

    @Query("SELECT SUM(i.quantity) FROM Inventory i WHERE i.product.productId = :productId AND i.inventoryStatus = 'STORAGECOMPLETED'")
    Long sumQuantityByProductIdAndStatus(@Param("productId") Long productId);


}
