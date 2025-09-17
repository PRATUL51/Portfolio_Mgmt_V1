package com.java.portfolio.controller;

import com.java.portfolio.response.PortfolioResponse;
import com.java.portfolio.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PortfolioController {
    @Autowired
    PortfolioService service;

    @GetMapping("/portfolio/test")
    public ResponseEntity<PortfolioResponse> viewAll(){
        return ResponseEntity.ok(service.viewAllPortfolio());
    }
}
