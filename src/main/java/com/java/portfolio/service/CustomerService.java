package com.java.portfolio.service;

import com.java.portfolio.response.CustomerResponse;
import org.springframework.stereotype.Service;

@Service
public interface CustomerService {
    public CustomerResponse viewALlCustomers();

    public CustomerResponse viewById(Long id);
}
