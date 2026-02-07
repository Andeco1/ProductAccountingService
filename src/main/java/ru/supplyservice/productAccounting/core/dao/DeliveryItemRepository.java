package ru.supplyservice.productAccounting.core.dao;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.supplyservice.productAccounting.core.entity.DeliveryItem;

import java.time.Instant;
import java.util.List;

public interface DeliveryItemRepository extends JpaRepository<DeliveryItem, Long> {
    @EntityGraph(attributePaths = {
            "product",
            "deliveryRecord",
            "deliveryRecord.supplier"
    })
    List<DeliveryItem> findAllByDeliveryRecordDateBetweenAndAcceptanceTrue(
            Instant start,
            Instant end
    );

//    "select * from DeliveryItem DI " +
//            "join DeliveryRecord DR " +
//            "on DR.id = DI.record_id " +
//            "where DR.date between :start and :end and AcceptanceIsTrue"

}
