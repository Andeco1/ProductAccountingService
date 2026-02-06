package ru.supplyservice.productAccounting.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.supplyservice.productAccounting.api.request.DeliveryRecordInfo;
import ru.supplyservice.productAccounting.core.dao.DeliveryRecordRepository;
import ru.supplyservice.productAccounting.core.entity.DeliveryRecord;
import ru.supplyservice.productAccounting.usecase.dto.DeliveryRecordDTO;
import ru.supplyservice.productAccounting.usecase.dto.SupplierDTO;
import ru.supplyservice.productAccounting.usecase.mapper.DTOMapper;

import java.util.List;

@Service
public class DeliveryRecordService {
    SupplierService supplierService;
    DeliveryRecordRepository deliveryRecordRepository;
    DeliveryItemService deliveryItemService;
    DTOMapper mapper;

    @Autowired
    public DeliveryRecordService(SupplierService supplierService, DeliveryRecordRepository deliveryRecordRepository, DeliveryItemService deliveryItemService, DTOMapper mapper) {
        this.supplierService = supplierService;
        this.deliveryRecordRepository = deliveryRecordRepository;
        this.deliveryItemService = deliveryItemService;
        this.mapper = mapper;
    }

    @Transactional
    public boolean saveDeliveryRecord(DeliveryRecordInfo deliveryRecordInfo){
        SupplierDTO supplierDTO = supplierService.getSupplierByName(deliveryRecordInfo.supplierName());
        DeliveryRecordDTO deliveryRecordDTO = new DeliveryRecordDTO(supplierDTO, deliveryRecordInfo.date(), deliveryRecordInfo.info());
        DeliveryRecord deliveryRecord = deliveryRecordRepository.save(mapper.deliveryRecordDTOtoDeliveryREcord(deliveryRecordDTO));
        deliveryRecordInfo.items().forEach(item -> deliveryItemService.saveDeliveryItem(deliveryRecord, item.productName(), item.quantity(), item.acceptance()));
        return true;
    }

    @Transactional
    public List<DeliveryRecord> getDeliveryRecords(){
        return deliveryRecordRepository.findAll();
    }
}
