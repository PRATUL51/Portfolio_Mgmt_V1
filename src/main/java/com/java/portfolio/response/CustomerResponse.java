package com.java.portfolio.response;

import com.java.portfolio.model.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CustomerResponse {
    int code;
    String status;
    String message;
    List<Customer> data;
}
