package com.java.portfolio.response;

import com.java.portfolio.model.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponse {
    int code;
    String status;
    String message;
    List<Customer> data;
}
