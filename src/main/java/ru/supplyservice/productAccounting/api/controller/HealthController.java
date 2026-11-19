package ru.supplyservice.productAccounting.api.controller;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

  @GetMapping("/")
  public Map<String, String> root() {
    return Map.of("app", "product-accounting", "status", "healthy");
  }

  @GetMapping("/health")
  public Map<String, String> health() {
    return Map.of("status", "healthy");
  }
}
