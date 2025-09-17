package com.java.portfolio.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioRequest {
    private Long portfolioId;
    private String panId;
    private String name;
    private String description;
}
