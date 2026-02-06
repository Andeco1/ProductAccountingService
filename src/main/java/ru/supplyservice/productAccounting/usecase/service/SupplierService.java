package ru.supplyservice.productAccounting.usecase.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.supplyservice.productAccounting.core.dao.SupplierRepository;
import ru.supplyservice.productAccounting.core.entity.Supplier;
import ru.supplyservice.productAccounting.exception.EntityNotFoundException;
import ru.supplyservice.productAccounting.usecase.dto.SupplierDTO;
import ru.supplyservice.productAccounting.usecase.mapper.DTOMapper;

import java.util.Optional;

@Service
public class SupplierService {
    SupplierRepository supplierRepository;
    DTOMapper mapper;

    @Autowired
    public SupplierService(SupplierRepository supplierRepository, DTOMapper mapper) {
        this.supplierRepository = supplierRepository;
        this.mapper = mapper;
    }
    @Transactional(readOnly = true)
    public SupplierDTO getSupplierByName(String name){
        Optional<Supplier> supplier = supplierRepository.findByName(name);
        return mapper.supplierToSupplierDTO(supplier.orElseThrow(() -> new EntityNotFoundException(Supplier.class.getName() + " наименования " + name + " не найден")));
    }
    @Transactional
    public SupplierDTO saveSupplier(SupplierDTO supplierDTO){
        Supplier supplier = supplierRepository.save(mapper.supplierDTOToSupplier(supplierDTO));
        return mapper.supplierToSupplierDTO(supplier);
    }

}
