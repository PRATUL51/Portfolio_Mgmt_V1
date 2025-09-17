package com.java.portfolio.response;

import com.java.portfolio.model.Portfolio;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioResponse {
    int code;
    String status;
    String message;
    List<Portfolio> data;
}
