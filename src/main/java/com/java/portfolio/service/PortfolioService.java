package com.java.portfolio.service;

import com.java.portfolio.request.PortfolioRequest;
import com.java.portfolio.response.PortfolioResponse;
import org.springframework.stereotype.Service;

@Service
public interface PortfolioService {

    public PortfolioResponse viewAllPortfolio();

    public PortfolioResponse addPortfolio(PortfolioRequest request);

    public PortfolioResponse viewPortfolioById(Long id);

    public PortfolioResponse updatePortfolio(PortfolioRequest request);

    public PortfolioResponse deletePortfolio(Long id);
}
