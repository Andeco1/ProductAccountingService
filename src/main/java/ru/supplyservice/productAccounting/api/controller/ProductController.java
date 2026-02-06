package ru.supplyservice.productAccounting.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.supplyservice.productAccounting.usecase.dto.ProductDTO;
import ru.supplyservice.productAccounting.usecase.service.ProductService;

@RestController
@RequestMapping("/api")
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping("/product/{name}")
    public ResponseEntity<ProductDTO> getProductByName(@PathVariable String name){
        return ResponseEntity.ok(productService.getProductByName(name));
    }
}
