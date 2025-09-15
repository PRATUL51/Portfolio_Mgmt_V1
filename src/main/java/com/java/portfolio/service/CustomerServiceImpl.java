package com.java.portfolio.service;

import com.java.portfolio.constants.ApplicationConstants;
import com.java.portfolio.entity.CustomerEntity;
import com.java.portfolio.model.Customer;
import com.java.portfolio.repository.CustomerRepository;
import com.java.portfolio.response.CustomerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    CustomerRepository repository;

    @Override
    public CustomerResponse viewALlCustomers() {
        List<CustomerEntity> data = repository.findAll();

        List<Customer> model = data.stream().map(dat -> new Customer(dat.getId(),dat.getFirstName(),
                dat.getLastName(),dat.getEmail(),dat.getPhoneNo(),dat.getBirthDate(),
                dat.getCreatedAt(),dat.getUpdatedAt()))
                .toList();

        if(model.isEmpty()){
            return new CustomerResponse(HttpStatus.NOT_FOUND.value(),HttpStatus.NOT_FOUND.name(),ApplicationConstants.NOT_FOUND_MSG,null);
        }else{
            return new CustomerResponse(HttpStatus.OK.value(), ApplicationConstants.SUCCESS,ApplicationConstants.SUCCESS_DATA_FETCHED_MSG,model);
        }
    }

    @Override
    public CustomerResponse viewById(String id) {
        CustomerEntity entity = repository.findById(id).orElse(null);

        if (entity == null) {
            return new CustomerResponse(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.name(), ApplicationConstants.ID_NOT_FOUND, null);
        } else {
            List<CustomerEntity> data = new ArrayList<>();
            data.add(entity);

            List<Customer> model = data.stream().map(dat -> new Customer(dat.getId(), dat.getFirstName(),
                            dat.getLastName(), dat.getEmail(), dat.getPhoneNo(), dat.getBirthDate(),
                            dat.getCreatedAt(), dat.getUpdatedAt()))
                    .toList();

            return new CustomerResponse(HttpStatus.OK.value(), ApplicationConstants.SUCCESS, ApplicationConstants.SUCCESS_DATA_FETCHED_MSG, model);
        }

    }
}
