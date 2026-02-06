package ru.supplyservice.productAccounting.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.supplyservice.productAccounting.usecase.dto.SupplierDTO;
import ru.supplyservice.productAccounting.usecase.service.SupplierService;

import java.net.URI;

@RestController
@RequestMapping("/api-v1.0/supplier")
public class SupplierController {

    SupplierService supplierService;
    @Autowired
    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping()
    public ResponseEntity<SupplierDTO> getSupplierByName(@RequestParam String name){
        return ResponseEntity.ok(supplierService.getSupplierByName(name));
    }

    @PostMapping()
    public ResponseEntity<SupplierDTO> uploadSupplier(@RequestBody SupplierDTO supplierDTO){
        supplierDTO = supplierService.saveSupplier(supplierDTO);
        return ResponseEntity.ok(supplierDTO);
    }
}
