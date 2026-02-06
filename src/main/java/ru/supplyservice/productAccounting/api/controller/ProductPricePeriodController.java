package ru.supplyservice.productAccounting.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.supplyservice.productAccounting.core.entity.ProductPricePeriod;
import ru.supplyservice.productAccounting.usecase.dto.ProductPricePeriodDTO;
import ru.supplyservice.productAccounting.usecase.service.ProductPricePeriodService;

import java.util.List;

@RestController
@RequestMapping("/api-v1.0/ppp")
public class ProductPricePeriodController {
    ProductPricePeriodService productPricePeriodService;
    @Autowired
    public ProductPricePeriodController(ProductPricePeriodService productPricePeriodService) {
        this.productPricePeriodService = productPricePeriodService;
    }
    @PostMapping()
    public ResponseEntity<ProductPricePeriodDTO> uploadProductPricePeriod(@RequestBody ProductPricePeriodDTO productPricePeriodDTO){
        productPricePeriodDTO = productPricePeriodService.saveProductPricePeriod(productPricePeriodDTO);
        return ResponseEntity.ok(productPricePeriodDTO);
    }
    @GetMapping("/all")
    public ResponseEntity<List<ProductPricePeriodDTO>> getAppPricePeriods(){
        return ResponseEntity.ok(productPricePeriodService.getAll());
    }
}
