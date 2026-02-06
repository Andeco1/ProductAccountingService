package ru.supplyservice.productAccounting.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.supplyservice.productAccounting.core.entity.DeliveryRecord;

@Repository
public interface DeliveryRecordRepository extends JpaRepository<DeliveryRecord, Long> {
}
