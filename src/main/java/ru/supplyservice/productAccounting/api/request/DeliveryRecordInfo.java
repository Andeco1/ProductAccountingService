package ru.supplyservice.productAccounting.api.request;

import java.time.Instant;
import java.util.List;

public record DeliveryRecordInfo(
        String supplierName,
        Instant date,
        String info,
        List<ItemsInfo> items
) {
}
