package com.shivam.trade_risk_engine.controller;

import com.shivam.trade_risk_engine.model.Portfolio;
import com.shivam.trade_risk_engine.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/portfolio")
@RequiredArgsConstructor
public class PortfolioController {

    private final PortfolioRepository portfolioRepository;

    // GET http://localhost:8080/api/portfolio/1
    @GetMapping("/{userId}")
    public ResponseEntity<List<Portfolio>> getUserPortfolio(@PathVariable Long userId) {
        return ResponseEntity.ok(portfolioRepository.findByUserId(userId));
    }
}