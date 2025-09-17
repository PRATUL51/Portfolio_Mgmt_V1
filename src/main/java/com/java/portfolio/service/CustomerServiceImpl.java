package com.java.portfolio.service;

import com.java.portfolio.constants.ApplicationConstants;
import com.java.portfolio.entity.CustomerEntity;
import com.java.portfolio.model.Customer;
import com.java.portfolio.repository.CustomerRepository;
import com.java.portfolio.request.CustomerRequest;
import com.java.portfolio.response.CustomerResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


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

    @Override
    @Transactional
    public CustomerResponse addCustomer(CustomerRequest request) {
        Optional<CustomerEntity> entity = repository.findById(request.getPanId());

        if(entity.isPresent()){
            return new CustomerResponse(HttpStatus.BAD_REQUEST.value(),ApplicationConstants.ALREADY_EXIST,ApplicationConstants.ID_EXIST_MSG,null);
        }else{
            CustomerEntity cust = new CustomerEntity();
            cust.setId(request.getPanId());
            cust.setFirstName(request.getFirstName());
            cust.setLastName(request.getLastName());
            cust.setBirthDate(request.getBirthDate());
            cust.setPhoneNo(request.getPhoneNo());
            cust.setEmail(request.getEmail());
            cust.setCreatedAt(LocalDateTime.now());

            repository.save(cust);
            return new CustomerResponse(HttpStatus.OK.value(), ApplicationConstants.SUCCESS, ApplicationConstants.SUCCESS_DATA_INSERT_MSG, null);
        }
    }

    @Override
    @Transactional
    public CustomerResponse updateCustomer(CustomerRequest request) {
        Optional<CustomerEntity> entity = repository.findById(request.getPanId());

        if(entity.isPresent()){
            CustomerEntity cust = entity.get();

            // update only if provided in request
            if (request.getFirstName() != null) {
                cust.setFirstName(request.getFirstName());
            }
            if (request.getLastName() != null) {
                cust.setLastName(request.getLastName());
            }
            if (request.getBirthDate() != null) {
                cust.setBirthDate(request.getBirthDate());
            }
            if (request.getPhoneNo() != null) {
                cust.setPhoneNo(request.getPhoneNo());
            }
            if (request.getEmail() != null) {
                cust.setEmail(request.getEmail());
            }
            cust.setUpdatedAt(LocalDateTime.now());

            repository.save(cust);
            return new CustomerResponse(HttpStatus.OK.value(), ApplicationConstants.SUCCESS, ApplicationConstants.SUCCESS_DATA_UPDATED_MSG, null);
        }else{
            return new CustomerResponse(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.name(), ApplicationConstants.ID_NOT_FOUND, null);
        }
    }

    @Override
    @Transactional
    public CustomerResponse deleteCustomer(String id) {
        Optional<CustomerEntity> entity = repository.findById(id);

        if(entity.isPresent()){
            repository.deleteById(id);
            return new CustomerResponse(HttpStatus.OK.value(), ApplicationConstants.SUCCESS, ApplicationConstants.SUCCESS_DATA_DELETED_MSG, null);
        }else{
            return new CustomerResponse(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.name(), ApplicationConstants.ID_NOT_FOUND, null);
        }
    }
}
