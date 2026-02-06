package ru.supplyservice.productAccounting.usecase.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.supplyservice.productAccounting.api.request.DeliveryRecordInfo;
import ru.supplyservice.productAccounting.core.dao.DeliveryRecordRepository;
import ru.supplyservice.productAccounting.core.entity.DeliveryRecord;
import ru.supplyservice.productAccounting.usecase.dto.DeliveryRecordDTO;
import ru.supplyservice.productAccounting.usecase.dto.SupplierDTO;
import ru.supplyservice.productAccounting.usecase.mapper.DTOMapper;

@Service
public class DeliveryRecordService {
    SupplierService supplierService;
    ProductService productService;
    DeliveryRecordRepository deliveryRecordRepository;
    DeliveryItemService deliveryItemService;
    DTOMapper mapper;

    @Transactional
    public boolean saveDeliveryRecord(DeliveryRecordInfo deliveryRecordInfo){
        SupplierDTO supplierDTO = supplierService.getSupplierByName(deliveryRecordInfo.supplierName());
        DeliveryRecordDTO deliveryRecordDTO = new DeliveryRecordDTO(supplierDTO, deliveryRecordInfo.date(), deliveryRecordInfo.info());
        DeliveryRecord deliveryRecord = deliveryRecordRepository.save(mapper.deliveryRecordDTOtoDeliveryREcord(deliveryRecordDTO));
        deliveryRecordInfo.items().forEach(item -> deliveryItemService.saveDeliveryItem(deliveryRecord, item.productName(), item.quantity(), item.acceptance()));
        return true;
    }
}
