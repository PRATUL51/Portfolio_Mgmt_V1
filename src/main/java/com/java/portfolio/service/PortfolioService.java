package com.java.portfolio.service;

import com.java.portfolio.response.PortfolioResponse;
import org.springframework.stereotype.Service;

@Service
public interface PortfolioService {

    public PortfolioResponse viewAllPortfolio();
}
