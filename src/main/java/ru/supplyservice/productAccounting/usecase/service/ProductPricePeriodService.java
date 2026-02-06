package ru.supplyservice.productAccounting.usecase.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.supplyservice.productAccounting.core.dao.ProductPricePeriodRepository;
import ru.supplyservice.productAccounting.core.entity.Product;
import ru.supplyservice.productAccounting.core.entity.ProductPricePeriod;
import ru.supplyservice.productAccounting.core.entity.Supplier;
import ru.supplyservice.productAccounting.exception.NoActivePriceFoundException;
import ru.supplyservice.productAccounting.usecase.dto.ProductPricePeriodDTO;
import ru.supplyservice.productAccounting.usecase.mapper.DTOMapper;

import java.time.Instant;
import java.util.Optional;

@Service
public class ProductPricePeriodService {
    DTOMapper mapper;
    ProductPricePeriodRepository productPricePeriodRepository;

    @Transactional(readOnly = true)
    public ProductPricePeriodDTO getActivePricePeriod(Product product, Supplier supplier, Instant date){
        Optional<ProductPricePeriod> productPricePeriod = productPricePeriodRepository.findActivePricePeriod(product,supplier,date);
        return mapper.productPricePeriodToProductPricePeriodDTO(productPricePeriod.orElseThrow(() -> new NoActivePriceFoundException("Не найдена цена для " + product.getName() + " поставщика " + supplier.getName() + " для даты " + date)));
    }
}
