package ru.supplyservice.productAccounting.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.supplyservice.productAccounting.api.request.DeliveryRecordsRequest;
import ru.supplyservice.productAccounting.core.entity.DeliveryRecord;
import ru.supplyservice.productAccounting.usecase.service.DeliveryRecordService;

import java.util.List;

@RestController
@RequestMapping("/api-v1.0/deliveryRecord")
public class DeliveryRecordsController {
    DeliveryRecordService deliveryRecordService;

    @Autowired
    public DeliveryRecordsController(DeliveryRecordService deliveryRecordService) {
        this.deliveryRecordService = deliveryRecordService;
    }

    @PostMapping()
    public ResponseEntity<String> uploadDeliveryRecords(@RequestBody DeliveryRecordsRequest request){
        request.deliveries().forEach(deliveryRecordInfo -> deliveryRecordService.saveDeliveryRecord(deliveryRecordInfo));
        return ResponseEntity.ok().build();
    }

    @GetMapping()
    public ResponseEntity<List<DeliveryRecord>> getDeliveryRecord(){
        return ResponseEntity.ok(deliveryRecordService.getDeliveryRecords());
    }
}
