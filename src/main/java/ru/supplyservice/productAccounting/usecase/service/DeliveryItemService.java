package ru.supplyservice.productAccounting.usecase.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.supplyservice.productAccounting.core.dao.DeliveryItemRepository;
import ru.supplyservice.productAccounting.core.entity.DeliveryItem;
import ru.supplyservice.productAccounting.core.entity.DeliveryRecord;
import ru.supplyservice.productAccounting.core.entity.Product;
import ru.supplyservice.productAccounting.core.entity.Supplier;
import ru.supplyservice.productAccounting.usecase.dto.ProductPricePeriodDTO;
import ru.supplyservice.productAccounting.usecase.mapper.DTOMapper;

import java.math.BigDecimal;
import java.time.Instant;

@Service
public class DeliveryItemService {
    ProductPricePeriodService productPricePeriodService;
    ProductService productService;
    DTOMapper mapper;
    DeliveryItemRepository deliveryItemRepository;
    @Transactional
    public DeliveryItem saveDeliveryItem(DeliveryRecord deliveryRecord, String productName, BigDecimal quantity, Boolean acceptance){
        Supplier supplier = deliveryRecord.getSupplier();
        Instant date = deliveryRecord.getDate();
        Product product = mapper.productDTOToProduct(productService.getProductByName(productName));
        ProductPricePeriodDTO productPricePeriodDTO = productPricePeriodService.getActivePricePeriod(product, supplier, date);
        BigDecimal priceOfAccepted = null;
        if(acceptance && productPricePeriodDTO != null){
            priceOfAccepted = quantity.multiply(productPricePeriodDTO.pricePerMeasurementUnit());
        }
        DeliveryItem deliveryItem = new DeliveryItem(product, deliveryRecord, quantity, acceptance, priceOfAccepted);
        return deliveryItemRepository.save(deliveryItem);
    }
}
