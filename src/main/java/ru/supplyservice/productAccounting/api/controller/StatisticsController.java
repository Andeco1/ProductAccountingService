package ru.supplyservice.productAccounting.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.supplyservice.productAccounting.api.response.ReportResponse;
import ru.supplyservice.productAccounting.usecase.service.StatisticService;

import java.time.Instant;

@RestController
@RequestMapping("/api-v1.0/statistics")
public class StatisticsController {
    @Autowired
    StatisticService statisticService;

    @GetMapping("/report")
    public ResponseEntity<ReportResponse> getReport(@RequestParam Instant dateFrom, @RequestParam Instant dateTo){
        return ResponseEntity.ok(statisticService.getReportOfPeriod(dateFrom, dateTo));
    }
}
