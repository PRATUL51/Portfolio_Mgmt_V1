package com.java.portfolio.service;

import com.java.portfolio.constants.ApplicationConstants;
import com.java.portfolio.entity.CustomerEntity;
import com.java.portfolio.model.Customer;
import com.java.portfolio.repository.CustomerRepository;
import com.java.portfolio.request.CustomerRequest;
import com.java.portfolio.response.CustomerResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CustomerServiceImplTest {

    @Mock
    private CustomerRepository repository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private CustomerEntity createCustomerEntity(String id, String firstName, String lastName, String email, Long phoneNo, LocalDate birthDate) {
        CustomerEntity entity = new CustomerEntity();
        entity.setId(id);
        entity.setFirstName(firstName);
        entity.setLastName(lastName);
        entity.setEmail(email);
        entity.setPhoneNo(phoneNo);
        entity.setBirthDate(birthDate);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }

    private CustomerRequest createCustomerRequest(String panId, String firstName, String lastName, String email, Long phoneNo, LocalDate birthDate) {
        CustomerRequest request = new CustomerRequest();
        request.setPanId(panId);
        request.setFirstName(firstName);
        request.setLastName(lastName);
        request.setEmail(email);
        request.setPhoneNo(phoneNo);
        request.setBirthDate(birthDate);
        return request;
    }

    @Test
    void viewALlCustomers_noCustomersFound() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        CustomerResponse response = customerService.viewALlCustomers();

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getCode());
        assertEquals(HttpStatus.NOT_FOUND.name(), response.getStatus());
        assertEquals(ApplicationConstants.NOT_FOUND_MSG, response.getMessage());
        verify(repository, times(1)).findAll();
    }

    @Test
    void viewALlCustomers_customersFound() {
        List<CustomerEntity> entities = new ArrayList<>();
        entities.add(createCustomerEntity("PAN123", "John", "Doe", "john.doe@example.com", 1234567890L, LocalDate.of(1990, 1, 1)));
        entities.add(createCustomerEntity("PAN456", "Jane", "Smith", "jane.smith@example.com", 9087654321L, LocalDate.of(1992, 2, 2)));

        when(repository.findAll()).thenReturn(entities);

        CustomerResponse response = customerService.viewALlCustomers();

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getCode());
        assertEquals(ApplicationConstants.SUCCESS, response.getStatus());
        assertEquals(ApplicationConstants.SUCCESS_DATA_FETCHED_MSG, response.getMessage());
        assertNotNull(response.getData());
        assertEquals(2, response.getData().size());
        assertEquals("John", ((Customer) response.getData().get(0)).getFirstName());
        verify(repository, times(1)).findAll();
    }

    @Test
    void viewById_customerNotFound() {
        String customerId = "NONEXISTENT";
        when(repository.findById(customerId)).thenReturn(Optional.empty());

        CustomerResponse response = customerService.viewById(customerId);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getCode());
        assertEquals(HttpStatus.NOT_FOUND.name(), response.getStatus());
        assertEquals(ApplicationConstants.ID_NOT_FOUND, response.getMessage());
        verify(repository, times(1)).findById(customerId);
    }

    @Test
    void viewById_customerFound() {
        String customerId = "PAN123";
        CustomerEntity entity = createCustomerEntity(customerId, "John", "Doe", "john.doe@example.com", 1234567890L, LocalDate.of(1990, 1, 1));
        when(repository.findById(customerId)).thenReturn(Optional.of(entity));

        CustomerResponse response = customerService.viewById(customerId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getCode());
        assertEquals(ApplicationConstants.SUCCESS, response.getStatus());
        assertEquals(ApplicationConstants.SUCCESS_DATA_FETCHED_MSG, response.getMessage());
        assertNotNull(response.getData());
        assertEquals(1, response.getData().size());
        assertEquals(customerId, ((Customer) response.getData().get(0)).getId());
        verify(repository, times(1)).findById(customerId);
    }

    @Test
    void addCustomer_idAlreadyExists() {
        CustomerRequest request = createCustomerRequest("PAN123", "John", "Doe", "john.doe@example.com", 1234567890L, LocalDate.of(1990, 1, 1));
        CustomerEntity existingEntity = createCustomerEntity("PAN123", "Existing", "User", "existing@example.com", 9000000000L, LocalDate.of(1980, 1, 1));

        when(repository.findById(request.getPanId())).thenReturn(Optional.of(existingEntity));

        CustomerResponse response = customerService.addCustomer(request);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getCode());
        assertEquals(HttpStatus.BAD_REQUEST.name(), response.getStatus());
        assertEquals(ApplicationConstants.ID_EXIST_MSG, response.getMessage());
        verify(repository, times(1)).findById(request.getPanId());
        verify(repository, never()).save(any(CustomerEntity.class));
    }

    @Test
    void addCustomer_success() {
        CustomerRequest request = createCustomerRequest("PAN123", "John", "Doe", "john.doe@example.com", 1234567890L, LocalDate.of(1990, 1, 1));

        when(repository.findById(request.getPanId())).thenReturn(Optional.empty());
        when(repository.save(any(CustomerEntity.class))).thenReturn(any(CustomerEntity.class)); // Mock save behavior

        CustomerResponse response = customerService.addCustomer(request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getCode());
        assertEquals(ApplicationConstants.SUCCESS, response.getStatus());
        assertEquals(ApplicationConstants.SUCCESS_DATA_INSERT_MSG, response.getMessage());
        verify(repository, times(1)).findById(request.getPanId());
        verify(repository, times(1)).save(any(CustomerEntity.class));
    }

    @Test
    void updateCustomer_customerNotFound() {
        CustomerRequest request = createCustomerRequest("NONEXISTENT", "John", "Doe", "john.doe@example.com", 1234567890L, LocalDate.of(1990, 1, 1));
        when(repository.findById(request.getPanId())).thenReturn(Optional.empty());

        CustomerResponse response = customerService.updateCustomer(request);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getCode());
        assertEquals(HttpStatus.NOT_FOUND.name(), response.getStatus());
        assertEquals(ApplicationConstants.ID_NOT_FOUND, response.getMessage());
        verify(repository, times(1)).findById(request.getPanId());
        verify(repository, never()).save(any(CustomerEntity.class));
    }

    @Test
    void updateCustomer_successAllFields() {
        String customerId = "PAN123";
        CustomerEntity existingEntity = createCustomerEntity(customerId, "OldFirstName", "OldLastName", "old@example.com", 9000000000L, LocalDate.of(1980, 1, 1));
        CustomerRequest request = createCustomerRequest(customerId, "NewFirstName", "NewLastName", "old@example.com", 9000000000L, LocalDate.of(1990, 2, 2));

        when(repository.findById(customerId)).thenReturn(Optional.of(existingEntity));
        when(repository.save(any(CustomerEntity.class))).thenReturn(existingEntity); // Return the updated entity

        CustomerResponse response = customerService.updateCustomer(request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getCode());
        assertEquals(ApplicationConstants.SUCCESS, response.getStatus());
        assertEquals(ApplicationConstants.SUCCESS_DATA_UPDATED_MSG, response.getMessage());

        verify(repository, times(1)).findById(customerId);
        verify(repository, times(1)).save(any(CustomerEntity.class));

        // Verify that fields were updated
        assertEquals(request.getFirstName(), existingEntity.getFirstName());
        assertEquals(request.getLastName(), existingEntity.getLastName());
        assertEquals(request.getEmail(), existingEntity.getEmail());
        assertEquals(request.getPhoneNo(), existingEntity.getPhoneNo());
        assertEquals(request.getBirthDate(), existingEntity.getBirthDate());
        assertNotNull(existingEntity.getUpdatedAt());
    }

    @Test
    void updateCustomer_successSomeFields() {
        String customerId = "PAN123";
        CustomerEntity existingEntity = createCustomerEntity(customerId, "OldFirstName", "OldLastName", "old@example.com", 9000000000L, LocalDate.of(1980, 1, 1));

        CustomerRequest request = new CustomerRequest(); // Only update email and phone
        request.setPanId(customerId);
        request.setEmail("updated@example.com");
        request.setPhoneNo(9999999999L);

        when(repository.findById(customerId)).thenReturn(Optional.of(existingEntity));
        when(repository.save(any(CustomerEntity.class))).thenReturn(existingEntity);

        CustomerResponse response = customerService.updateCustomer(request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getCode());
        assertEquals(ApplicationConstants.SUCCESS, response.getStatus());
        assertEquals(ApplicationConstants.SUCCESS_DATA_UPDATED_MSG, response.getMessage());

        verify(repository, times(1)).findById(customerId);
        verify(repository, times(1)).save(any(CustomerEntity.class));

        // Verify only specified fields were updated
        assertEquals("OldFirstName", existingEntity.getFirstName()); // Should remain old
        assertEquals("OldLastName", existingEntity.getLastName());   // Should remain old
        assertEquals("updated@example.com", existingEntity.getEmail());
        assertEquals(9999999999L, existingEntity.getPhoneNo());
        assertEquals(LocalDate.of(1980, 1, 1), existingEntity.getBirthDate()); // Should remain old
        assertNotNull(existingEntity.getUpdatedAt());
    }

    @Test
    void deleteCustomer_customerNotFound() {
        String customerId = "NONEXISTENT";
        when(repository.findById(customerId)).thenReturn(Optional.empty());

        CustomerResponse response = customerService.deleteCustomer(customerId);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getCode());
        assertEquals(HttpStatus.NOT_FOUND.name(), response.getStatus());
        assertEquals(ApplicationConstants.ID_NOT_FOUND, response.getMessage());
        verify(repository, times(1)).findById(customerId);
        verify(repository, never()).deleteById(any(String.class));
    }

    @Test
    void deleteCustomer_success() {
        String customerId = "PAN123";
        CustomerEntity existingEntity = createCustomerEntity(customerId, "John", "Doe", "john.doe@example.com", 1234567890L, LocalDate.of(1990, 1, 1));
        when(repository.findById(customerId)).thenReturn(Optional.of(existingEntity));

        CustomerResponse response = customerService.deleteCustomer(customerId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getCode());
        assertEquals(ApplicationConstants.SUCCESS, response.getStatus());
        assertEquals(ApplicationConstants.SUCCESS_DATA_DELETED_MSG, response.getMessage());
        verify(repository, times(1)).findById(customerId);
        verify(repository, times(1)).deleteById(customerId);
    }
}