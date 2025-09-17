package com.java.portfolio.controller;

import com.java.portfolio.request.CustomerRequest;
import com.java.portfolio.response.CustomerResponse;
import com.java.portfolio.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class CustomerController {
    @Autowired
    CustomerService service;

    @GetMapping("/customer/test")
    public ResponseEntity<CustomerResponse> viewAll(){
        return ResponseEntity.ok(service.viewALlCustomers());
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<CustomerResponse> fetchById(@PathVariable("id") String id){
        return ResponseEntity.ok(service.viewById(id));
    }

    @PostMapping("/customer")
    public ResponseEntity<CustomerResponse> addCust(@RequestBody CustomerRequest request){
        return ResponseEntity.ok(service.addCustomer(request));
    }

    @PatchMapping("/customer/test")
    public ResponseEntity<CustomerResponse> updateCust(@RequestBody CustomerRequest request){
        return ResponseEntity.ok(service.updateCustomer(request));
    }

    @DeleteMapping("/customer/{id}/test")
    public ResponseEntity<CustomerResponse> deleteCust(@PathVariable String id){
        return ResponseEntity.ok(service.deleteCustomer(id));
    }


}
