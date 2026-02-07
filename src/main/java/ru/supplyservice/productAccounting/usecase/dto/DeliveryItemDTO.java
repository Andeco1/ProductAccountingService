package ru.supplyservice.productAccounting.usecase.dto;

import java.math.BigDecimal;

public record DeliveryItemDTO(
        DeliveryRecordDTO deliveryRecordDTO,
        ProductDTO productDTO,
        BigDecimal quantity,
        boolean acceptance,
        BigDecimal priceOfAccepted
) {
}
