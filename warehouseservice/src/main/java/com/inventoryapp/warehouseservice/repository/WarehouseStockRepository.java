package com.inventoryapp.warehouseservice.repository;
import com.inventoryapp.warehouseservice.model.WarehouseStock;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface WarehouseStockRepository extends JpaRepository<WarehouseStock, Long> {
    Optional<WarehouseStock> findByWarehouseIdAndProductId(Long warehouseId, Long productId);
    List<WarehouseStock> findByWarehouseId(Long warehouseId);
}