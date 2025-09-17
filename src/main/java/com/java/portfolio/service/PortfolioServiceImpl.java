package com.java.portfolio.service;

import com.java.portfolio.constants.ApplicationConstants;
import com.java.portfolio.entity.CustomerEntity;
import com.java.portfolio.entity.PortfolioEntity;
import com.java.portfolio.model.Portfolio;
import com.java.portfolio.repository.CustomerRepository;
import com.java.portfolio.repository.PortfolioRepository;
import com.java.portfolio.request.PortfolioRequest;
import com.java.portfolio.response.PortfolioResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PortfolioServiceImpl implements PortfolioService{
    @Autowired
    PortfolioRepository repository;

    @Autowired
    CustomerRepository customerRepository;

    @Override
    public PortfolioResponse viewAllPortfolio() {
        List<PortfolioEntity> entity = repository.findAll();

        if(entity.isEmpty()){
            return new PortfolioResponse(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.name(), ApplicationConstants.NOT_FOUND_MSG,null);
        }else{
            List<Portfolio> model = entity
                    .stream()
                    .map(data -> new Portfolio(data.getPortfolioId(),data.getCustomer().getId(),data.getName(), data.getDescription(),data.getCreatedAt(), data.getUpdatedAt()))
                    .toList();

            return new PortfolioResponse(HttpStatus.OK.value(),ApplicationConstants.SUCCESS,ApplicationConstants.SUCCESS_DATA_FETCHED_MSG,model);
        }
    }

    @Override
    @Transactional
    public PortfolioResponse addPortfolio(PortfolioRequest request) {
        Optional<PortfolioEntity> entity = repository.findById(request.getPortfolioId());
        if(entity.isPresent()){
            return new PortfolioResponse(HttpStatus.BAD_REQUEST.value(),ApplicationConstants.ALREADY_EXIST,ApplicationConstants.ID_EXIST_MSG,null);
        }else{
            PortfolioEntity portfolioEntity = new PortfolioEntity();

            portfolioEntity.setPortfolioId(request.getPortfolioId());
            CustomerEntity custEntity = customerRepository.findById(request.getPanId()).orElse(null);
            if(custEntity == null){
                return new PortfolioResponse(HttpStatus.BAD_REQUEST.value(),HttpStatus.BAD_REQUEST.name(),request.getPanId()+ApplicationConstants.PAN_ID_NOT_FOUND_DATA,null);
            }
            portfolioEntity.setCustomer(custEntity);
            portfolioEntity.setName(request.getName());
            portfolioEntity.setDescription(request.getDescription());
            portfolioEntity.setCreatedAt(LocalDateTime.now());

            repository.save(portfolioEntity);

            return new PortfolioResponse(HttpStatus.OK.value(), ApplicationConstants.SUCCESS, ApplicationConstants.SUCCESS_DATA_INSERT_MSG, null);
        }
    }

    @Override
    public PortfolioResponse viewPortfolioById(Long id) {
        PortfolioEntity entity = repository.findById(id).orElse(null);

        if(entity == null){
            return new PortfolioResponse(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.name(), ApplicationConstants.NOT_FOUND_MSG,null);
        }else{
            Portfolio model = new Portfolio(entity.getPortfolioId(),
                    entity.getCustomer().getId(), entity.getName(),
                    entity.getDescription(), entity.getCreatedAt(),
                    entity.getUpdatedAt());

            return new PortfolioResponse(HttpStatus.OK.value(), ApplicationConstants.SUCCESS, ApplicationConstants.SUCCESS_DATA_FETCHED_MSG, List.of(model));
        }
    }

    @Override
    @Transactional
    public PortfolioResponse updatePortfolio(PortfolioRequest request) {
        PortfolioEntity entity = repository.findById(request.getPortfolioId()).orElse(null);

        if(entity == null){
            return new PortfolioResponse(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.name(), ApplicationConstants.NOT_FOUND_MSG,null);
        }else {
            if(request.getName() != null) {
                entity.setName(request.getName());
            }
            if(request.getDescription() != null) {
                entity.setDescription(request.getDescription());
            }
            entity.setUpdatedAt(LocalDateTime.now());

            repository.save(entity);

            return new PortfolioResponse(HttpStatus.OK.value(), ApplicationConstants.SUCCESS, ApplicationConstants.SUCCESS_DATA_UPDATED_MSG, null);
        }
    }

    @Override
    @Transactional
    public PortfolioResponse deletePortfolio(Long id) {
        PortfolioEntity entity = repository.findById(id).orElse(null);

        if(entity == null){
            return new PortfolioResponse(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.name(), ApplicationConstants.NOT_FOUND_MSG,null);
        }else {
            repository.deleteById(id);
            return new PortfolioResponse(HttpStatus.OK.value(), ApplicationConstants.SUCCESS, ApplicationConstants.SUCCESS_DATA_DELETED_MSG, null);
        }
    }


}
