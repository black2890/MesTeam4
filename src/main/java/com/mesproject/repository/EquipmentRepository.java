package com.mesproject.repository;

import com.mesproject.dto.EquipmentDto;
import com.mesproject.entity.Equipment;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    @Query("SELECT NEW com.mesproject.dto.EquipmentDto(e.equipmentId, e.equipmentName, pc.processType, e.product, CONCAT(e.productionCapacity, e.productionCapacityUnit), CONCAT(e.setupTime, 'min'), CONCAT(e.cycleHour,'h'), e.acquisitionDate, e.equipmentStatus) " +
            "FROM Equipment e " +
            "LEFT JOIN e.process pc")
    Page<EquipmentDto> findEquipment(Pageable pageable);

    @Query("SELECT NEW com.mesproject.dto.EquipmentDto(e.equipmentId, e.equipmentName, pc.processType, e.product, CONCAT(e.productionCapacity, e.productionCapacityUnit), CONCAT(e.setupTime, 'min'), CONCAT(e.cycleHour,'h'), e.acquisitionDate, e.equipmentStatus) " +
            "FROM Equipment e " +
            "LEFT JOIN e.process pc " +
            "WHERE (:searchType = '설비명' AND e.equipmentName LIKE %:searchValue%) OR " +
            "      (:searchType = '생산제품' AND e.product LIKE %:searchValue%)")
    Page<EquipmentDto> findEquipmentBySearchOption(@Param("searchType") String searchType,
                                                       @Param("searchValue") String searchValue,
                                                       Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE Equipment e SET e.equipmentStatus = 'AVAILABLE' WHERE e.equipmentId = :equipmentId")
    void updateEquipmentStatusToAvailable(@Param("equipmentId") Long equipmentId);

    @Modifying
    @Transactional
    @Query("UPDATE Equipment e SET e.equipmentStatus = 'UNAVAILABLE' WHERE e.equipmentId = :equipmentId")
    void updateEquipmentStatusToUnavailable(@Param("equipmentId") Long equipmentId);
}
