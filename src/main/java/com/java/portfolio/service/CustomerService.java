package com.java.portfolio.service;

import com.java.portfolio.request.CustomerRequest;
import com.java.portfolio.response.CustomerResponse;
import org.springframework.stereotype.Service;

@Service
public interface CustomerService {
    public CustomerResponse viewALlCustomers();

    public CustomerResponse viewById(String id);

    public CustomerResponse addCustomer(CustomerRequest request);

    public CustomerResponse updateCustomer(CustomerRequest request);

    public CustomerResponse deleteCustomer(String id);
}
