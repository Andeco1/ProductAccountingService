package ru.supplyservice.productAccounting.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.supplyservice.productAccounting.usecase.dto.ProductDTO;
import ru.supplyservice.productAccounting.usecase.service.ProductService;

@RestController
@RequestMapping("/api-v1.0/product")
public class ProductController {
  ProductService productService;

  @Autowired
  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping()
  public ResponseEntity<ProductDTO> getProductByName(@RequestParam String name) {
    return ResponseEntity.ok(productService.getProductByName(name));
  }

  @PostMapping()
  public ResponseEntity<ProductDTO> uploadProduct(@RequestBody ProductDTO productDTO) {
    productDTO = productService.saveProduct(productDTO);
    return ResponseEntity.ok(productDTO);
  }
}
