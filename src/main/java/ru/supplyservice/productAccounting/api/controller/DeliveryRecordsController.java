package ru.supplyservice.productAccounting.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.supplyservice.productAccounting.api.request.DeliveryRecordsRequest;
import ru.supplyservice.productAccounting.usecase.service.DeliveryRecordService;

@RestController
@RequestMapping("/api-v1.0")
public class DeliveryRecordsController {

    DeliveryRecordService deliveryRecordService;

    @PostMapping("/deliveryRecord")
    public ResponseEntity<String> loadDeliveryRecords(DeliveryRecordsRequest request){
        request.deliveries().forEach(deliveryRecordInfo -> deliveryRecordService.saveDeliveryRecord(deliveryRecordInfo));
        return ResponseEntity.ok().build();
    }
}
