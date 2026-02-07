package ru.supplyservice.productAccounting.api.controller;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.supplyservice.productAccounting.api.response.ReportResponse;
import ru.supplyservice.productAccounting.usecase.service.StatisticService;

@RestController
@RequestMapping("/api-v1.0/statistics")
public class StatisticsController {
  StatisticService statisticService;

  @Autowired
  public StatisticsController(StatisticService statisticService) {
    this.statisticService = statisticService;
  }

  @GetMapping("/report")
  public ResponseEntity<ReportResponse> getReport(
      @RequestParam LocalDate dateFrom, @RequestParam LocalDate dateTo) {
    Instant start = dateFrom.atStartOfDay(ZoneId.systemDefault()).toInstant();
    Instant end = dateTo.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant();
    return ResponseEntity.ok(statisticService.getReportOfPeriod(start, end));
  }
}
