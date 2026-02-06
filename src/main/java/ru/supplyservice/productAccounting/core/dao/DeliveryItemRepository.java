package ru.supplyservice.productAccounting.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.supplyservice.productAccounting.core.entity.DeliveryItem;

public interface DeliveryItemRepository extends JpaRepository<DeliveryItem, Long> {
}
