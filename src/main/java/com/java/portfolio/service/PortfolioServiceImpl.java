package com.java.portfolio.service;

import com.java.portfolio.constants.ApplicationConstants;
import com.java.portfolio.entity.PortfolioEntity;
import com.java.portfolio.model.Portfolio;
import com.java.portfolio.repository.PortfolioRepository;
import com.java.portfolio.response.PortfolioResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PortfolioServiceImpl implements PortfolioService{
    @Autowired
    PortfolioRepository repository;


    @Override
    public PortfolioResponse viewAllPortfolio() {
        List<PortfolioEntity> entity = repository.findAll();

        if(entity.isEmpty()){
            return new PortfolioResponse(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.name(), ApplicationConstants.NOT_FOUND_MSG,null);
        }else{
            List<Portfolio> model = entity
                    .stream()
                    .map(data -> new Portfolio(data.getPortfolioId(),data.getCustomer().getId(),data.getName(), data.getDescription(),data.getCreatedAt()))
                    .toList();

            return new PortfolioResponse(HttpStatus.OK.value(),ApplicationConstants.SUCCESS,ApplicationConstants.SUCCESS_DATA_FETCHED_MSG,model);
        }
    }
}
