package com.java.portfolio.controller;

import com.java.portfolio.request.PortfolioRequest;
import com.java.portfolio.response.PortfolioResponse;
import com.java.portfolio.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class PortfolioController {
    @Autowired
    PortfolioService service;

    @GetMapping("/portfolio/test")
    public ResponseEntity<PortfolioResponse> viewAll(){
        return ResponseEntity.ok(service.viewAllPortfolio());
    }

    @PostMapping("/portfolio")
    public ResponseEntity<PortfolioResponse> addPortfolio(@RequestBody PortfolioRequest request){
        return ResponseEntity.ok(service.addPortfolio(request));
    }

    @GetMapping("/portfolio/{id}")
    public ResponseEntity<PortfolioResponse> viewById(@PathVariable Long id){
        return ResponseEntity.ok(service.viewPortfolioById(id));
    }

    @PatchMapping("/portfolio/test")
    public ResponseEntity<PortfolioResponse> updatePortfolio(@RequestBody PortfolioRequest request){
        return ResponseEntity.ok(service.updatePortfolio(request));
    }

    @DeleteMapping("/portfolio/{id}/test")
    public ResponseEntity<PortfolioResponse> deletePortfolioById(@PathVariable Long id){
        return ResponseEntity.ok(service.deletePortfolio(id));
    }
}
