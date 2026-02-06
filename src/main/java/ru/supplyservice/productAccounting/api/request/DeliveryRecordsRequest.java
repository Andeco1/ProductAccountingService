package ru.supplyservice.productAccounting.api.request;

import java.util.List;

public record DeliveryRecordsRequest(
    List<DeliveryRecordInfo> deliveries
) {
}
