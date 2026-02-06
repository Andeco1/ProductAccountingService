package ru.supplyservice.productAccounting.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.supplyservice.productAccounting.core.dao.ProductRepository;
import ru.supplyservice.productAccounting.core.entity.Product;
import ru.supplyservice.productAccounting.exception.FruitAccountingException;
import ru.supplyservice.productAccounting.usecase.dto.ProductDTO;
import ru.supplyservice.productAccounting.usecase.mapper.DTOMapper;

import java.util.Optional;

@Service
public class ProductService {
    ProductRepository productRepository;
    DTOMapper mapper;

    @Autowired
    public ProductService(ProductRepository productRepository, DTOMapper mapper) {
        this.productRepository = productRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public ProductDTO getProductByName(String name){
        Optional<Product> product = productRepository.findByName(name);
        return mapper.productToProductDTO(product.orElseThrow(() -> new FruitAccountingException(name + " не найден")));
    }
}
