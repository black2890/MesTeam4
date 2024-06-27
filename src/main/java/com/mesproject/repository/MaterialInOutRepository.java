package com.mesproject.repository;

import com.mesproject.entity.MaterialInOut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MaterialInOutRepository extends JpaRepository<MaterialInOut, Long> {
   List<MaterialInOut> findByWorkOrders_WorkOrderId(Long id);
   Optional<MaterialInOut> findByMaterialOrders_MaterialOrderId(Long id);
   List<MaterialInOut> findAllByProduct_ProductIdOrderByStorageDateAsc(Long id);
   @Query("SELECT SUM(m.quantity) FROM MaterialInOut m WHERE m.product.productId = :productId AND m.materialInOutStatus = 'STORAGE'")
   Long sumQuantityByProductIdAndStatus(@Param("productId") Long productId);

   @Query("SELECT m.product.productName, SUM(m.quantity) FROM MaterialInOut m WHERE m.materialInOutStatus = 'STORAGE' GROUP BY m.product.productName")
   List<Object[]> findMaterialTotalStockByProductNameAndStatus();

}
