package ru.supplyservice.productAccounting.usecase.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.supplyservice.productAccounting.core.dao.ProductPricePeriodRepository;
import ru.supplyservice.productAccounting.core.entity.Product;
import ru.supplyservice.productAccounting.core.entity.ProductPricePeriod;
import ru.supplyservice.productAccounting.core.entity.Supplier;
import ru.supplyservice.productAccounting.exception.NoActivePriceFoundException;
import ru.supplyservice.productAccounting.usecase.dto.ProductDTO;
import ru.supplyservice.productAccounting.usecase.dto.ProductPricePeriodDTO;
import ru.supplyservice.productAccounting.usecase.mapper.DTOMapper;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class ProductPricePeriodService {
    DTOMapper mapper;
    ProductPricePeriodRepository productPricePeriodRepository;
    ProductService productService;
    SupplierService supplierService;
    @Autowired
    public ProductPricePeriodService(SupplierService supplierService, ProductService productService, ProductPricePeriodRepository productPricePeriodRepository, DTOMapper mapper) {
        this.supplierService = supplierService;
        this.productService = productService;
        this.productPricePeriodRepository = productPricePeriodRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public ProductPricePeriodDTO getActivePricePeriod(Product product, Supplier supplier, Instant date){
        List<ProductPricePeriod> productPricePeriod = productPricePeriodRepository.findActivePricePeriod(product, supplier,date);
        if(productPricePeriod.isEmpty()){
            throw new NoActivePriceFoundException("Не найдена цена для " + product.getName() + " поставщика " + supplier.getName() + " для даты " + date);
        } else if (productPricePeriod.size() > 1){
            throw new NoActivePriceFoundException("Найдено несколько цен для " + product.getName() + " поставщика " + supplier.getName() + " для даты " + date);
        }
        return mapper.productPricePeriodToProductPricePeriodDTO(productPricePeriod.getFirst());
    }

    @Transactional
    public ProductPricePeriodDTO saveProductPricePeriod(ProductPricePeriodDTO productPricePeriodDTO){
        Product product = mapper.productDTOToProduct(productService.getProductByName(productPricePeriodDTO.productName()));
        Supplier supplier = mapper.supplierDTOToSupplier(supplierService.getSupplierByName(productPricePeriodDTO.supplierName()));
        ProductPricePeriod productPricePeriod = productPricePeriodRepository.save(new ProductPricePeriod(supplier,product,productPricePeriodDTO.startDate(), productPricePeriodDTO.endDate(), productPricePeriodDTO.pricePerMeasurementUnit()));
        return mapper.productPricePeriodToProductPricePeriodDTO(productPricePeriod);
    }

    @Transactional(readOnly = true)
    public List<ProductPricePeriodDTO> getAll(){
        return productPricePeriodRepository.findAll().stream().map(mapper::productPricePeriodToProductPricePeriodDTO).toList();
    }
}
