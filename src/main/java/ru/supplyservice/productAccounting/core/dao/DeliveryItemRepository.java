package ru.supplyservice.productAccounting.core.dao;

import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.supplyservice.productAccounting.core.entity.DeliveryItem;

public interface DeliveryItemRepository extends JpaRepository<DeliveryItem, Long> {
  @EntityGraph(attributePaths = {"product", "deliveryRecord", "deliveryRecord.supplier"})
  List<DeliveryItem> findAllByDeliveryRecordDateBetweenAndAcceptanceTrue(
      Instant start, Instant end);
}
