package com.mesproject.repository;

import com.mesproject.entity.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {


    @Query("SELECT i FROM Inventory i JOIN i.product p WHERE p.productName LIKE %:productName%")
    Page<Inventory> findAllByProductNameContaining(String productName, Pageable pageable);

    Page<Inventory> findAllByProduct_ProductNameContaining(String searchValue, Pageable pageable);
}
