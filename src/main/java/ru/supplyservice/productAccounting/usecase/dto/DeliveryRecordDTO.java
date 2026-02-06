package ru.supplyservice.productAccounting.usecase.dto;


import java.time.Instant;

public record DeliveryRecordDTO(
        SupplierDTO supplierDTO,
        Instant date,
        String info) {
}
