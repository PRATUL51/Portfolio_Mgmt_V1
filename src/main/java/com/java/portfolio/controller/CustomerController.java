package com.java.portfolio.controller;

import com.java.portfolio.response.CustomerResponse;
import com.java.portfolio.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CustomerController {
    @Autowired
    CustomerService service;

    @GetMapping("/customers/test")
    public ResponseEntity<CustomerResponse> viewAll(){
        return ResponseEntity.ok(service.viewALlCustomers());
    }

    @GetMapping("/customers/{id}")
    public ResponseEntity<CustomerResponse> fetchById(@PathVariable("id") String id){
        return ResponseEntity.ok(service.viewById(id));
    }


}
