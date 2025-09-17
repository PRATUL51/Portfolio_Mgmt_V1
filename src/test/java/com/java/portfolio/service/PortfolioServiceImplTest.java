package com.java.portfolio.service;

import com.java.portfolio.constants.ApplicationConstants;
import com.java.portfolio.entity.CustomerEntity;
import com.java.portfolio.entity.PortfolioEntity;
import com.java.portfolio.model.Portfolio;
import com.java.portfolio.repository.CustomerRepository;
import com.java.portfolio.repository.PortfolioRepository;
import com.java.portfolio.request.PortfolioRequest;
import com.java.portfolio.response.PortfolioResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PortfolioServiceImplTest {
    @Mock
    PortfolioRepository portfolioRepository;
    @Mock
    CustomerRepository customerRepository;
    @InjectMocks
    PortfolioServiceImpl portfolioService;
    private CustomerEntity customerEntity;
    private PortfolioEntity portfolioEntity;
    private PortfolioRequest portfolioRequest;

    @BeforeEach
    void setUp() {
        customerEntity = new CustomerEntity();
        customerEntity.setId("PAN123");
        portfolioEntity = new PortfolioEntity();
        portfolioEntity.setPortfolioId(1L);
        portfolioEntity.setCustomer(customerEntity);
        portfolioEntity.setName("Investment Portfolio");
        portfolioEntity.setDescription("Long term investments");
        portfolioEntity.setCreatedAt(LocalDateTime.now());
        portfolioRequest = new PortfolioRequest();
        portfolioRequest.setPortfolioId(1L);
        portfolioRequest.setPanId("PAN123");
        portfolioRequest.setName("Investment Portfolio");
        portfolioRequest.setDescription("Long term investments");
    }

    @Test
    void viewAllPortfolio_NoPortfolioFound_ReturnsNotFoundResponse() {
        when(portfolioRepository.findAll()).thenReturn(Collections.emptyList());
        PortfolioResponse response = portfolioService.viewAllPortfolio();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getCode());
        assertEquals(HttpStatus.NOT_FOUND.name(), response.getStatus());
        assertEquals(ApplicationConstants.NOT_FOUND_MSG, response.getMessage());
        assertNull(response.getData());
        verify(portfolioRepository, times(1)).findAll();
    }

    @Test
    void viewAllPortfolio_PortfolioFound_ReturnsOkResponseWithData() {
        PortfolioEntity portfolioEntity2 = new PortfolioEntity();
        portfolioEntity2.setPortfolioId(2L);
        portfolioEntity2.setCustomer(customerEntity);
        portfolioEntity2.setName("Savings Portfolio");
        portfolioEntity2.setDescription("Short term savings");
        portfolioEntity2.setCreatedAt(LocalDateTime.now());
        List<PortfolioEntity> portfolioEntities = Arrays.asList(portfolioEntity, portfolioEntity2);
        when(portfolioRepository.findAll()).thenReturn(portfolioEntities);
        PortfolioResponse response = portfolioService.viewAllPortfolio();
        assertEquals(HttpStatus.OK.value(), response.getCode());
        assertEquals(ApplicationConstants.SUCCESS, response.getStatus());
        assertEquals(ApplicationConstants.SUCCESS_DATA_FETCHED_MSG, response.getMessage());
        assertNotNull(response.getData());
        assertEquals(2, response.getData().size());
        assertEquals(portfolioEntity.getPortfolioId(), response.getData().get(0).getPortfolioId());
        assertEquals(portfolioEntity2.getPortfolioId(), response.getData().get(1).getPortfolioId());
        verify(portfolioRepository, times(1)).findAll();
    }

    @Test
    void addPortfolio_PortfolioIdAlreadyExists_ReturnsBadRequestResponse() {
        when(portfolioRepository.findById(anyLong())).thenReturn(Optional.of(portfolioEntity));
        PortfolioResponse response = portfolioService.addPortfolio(portfolioRequest);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getCode());
        assertEquals(ApplicationConstants.ALREADY_EXIST, response.getStatus());
        assertEquals(ApplicationConstants.ID_EXIST_MSG, response.getMessage());
        assertNull(response.getData());
        verify(portfolioRepository, times(1)).findById(anyLong());
        verify(customerRepository, never()).findById(anyString());
        verify(portfolioRepository, never()).save(any(PortfolioEntity.class));
    }

    @Test
    void addPortfolio_CustomerPanIdNotFound_ReturnsBadRequestResponse() {
        when(portfolioRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(customerRepository.findById(anyString())).thenReturn(Optional.empty());
        PortfolioResponse response = portfolioService.addPortfolio(portfolioRequest);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getCode());
        assertEquals(HttpStatus.BAD_REQUEST.name(), response.getStatus());
        assertEquals(portfolioRequest.getPanId() + ApplicationConstants.PAN_ID_NOT_FOUND_DATA, response.getMessage());
        assertNull(response.getData());
        verify(portfolioRepository, times(1)).findById(anyLong());
        verify(customerRepository, times(1)).findById(anyString());
        verify(portfolioRepository, never()).save(any(PortfolioEntity.class));
    }

    @Test
    void addPortfolio_SuccessfulAddition_ReturnsOkResponse() {
        when(portfolioRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(customerRepository.findById(anyString())).thenReturn(Optional.of(customerEntity));
        when(portfolioRepository.save(any(PortfolioEntity.class))).thenReturn(portfolioEntity);
        PortfolioResponse response = portfolioService.addPortfolio(portfolioRequest);
        assertEquals(HttpStatus.OK.value(), response.getCode());
        assertEquals(ApplicationConstants.SUCCESS, response.getStatus());
        assertEquals(ApplicationConstants.SUCCESS_DATA_INSERT_MSG, response.getMessage());
        assertNull(response.getData());
        verify(portfolioRepository, times(1)).findById(anyLong());
        verify(customerRepository, times(1)).findById(anyString());
        verify(portfolioRepository, times(1)).save(any(PortfolioEntity.class));
    }

    @Test
    void viewPortfolioById_PortfolioNotFound_ReturnsNotFoundResponse() {
        when(portfolioRepository.findById(anyLong())).thenReturn(Optional.empty());
        PortfolioResponse response = portfolioService.viewPortfolioById(1L);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getCode());
        assertEquals(HttpStatus.NOT_FOUND.name(), response.getStatus());
        assertEquals(ApplicationConstants.NOT_FOUND_MSG, response.getMessage());
        assertNull(response.getData());
        verify(portfolioRepository, times(1)).findById(anyLong());
    }

    @Test
    void viewPortfolioById_PortfolioFound_ReturnsOkResponseWithData() {
        when(portfolioRepository.findById(anyLong())).thenReturn(Optional.of(portfolioEntity));
        PortfolioResponse response = portfolioService.viewPortfolioById(1L);
        assertEquals(HttpStatus.OK.value(), response.getCode());
        assertEquals(ApplicationConstants.SUCCESS, response.getStatus());
        assertEquals(ApplicationConstants.SUCCESS_DATA_FETCHED_MSG, response.getMessage());
        assertNotNull(response.getData());
        assertEquals(1, response.getData().size());
        Portfolio actualPortfolio = response.getData().get(0);
        assertEquals(portfolioEntity.getPortfolioId(), actualPortfolio.getPortfolioId());
        assertEquals(portfolioEntity.getName(), actualPortfolio.getName());
        assertEquals(portfolioEntity.getDescription(), actualPortfolio.getDescription());
        assertEquals(portfolioEntity.getCustomer().getId(), actualPortfolio.getPanId());
        verify(portfolioRepository, times(1)).findById(anyLong());
    }

    @Test
    void updatePortfolio_PortfolioNotFound_ReturnsNotFoundResponse() {
        when(portfolioRepository.findById(anyLong())).thenReturn(Optional.empty());
        PortfolioResponse response = portfolioService.updatePortfolio(portfolioRequest);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getCode());
        assertEquals(HttpStatus.NOT_FOUND.name(), response.getStatus());
        assertEquals(ApplicationConstants.NOT_FOUND_MSG, response.getMessage());
        assertNull(response.getData());
        verify(portfolioRepository, times(1)).findById(anyLong());
        verify(portfolioRepository, never()).save(any(PortfolioEntity.class));
    }

    @Test
    void updatePortfolio_UpdateAllFields_ReturnsOkResponse() {
        PortfolioRequest updateRequest = new PortfolioRequest();
        updateRequest.setPortfolioId(1L);
        updateRequest.setName("Updated Portfolio Name");
        updateRequest.setDescription("Updated description for portfolio");
        when(portfolioRepository.findById(anyLong())).thenReturn(Optional.of(portfolioEntity));
        when(portfolioRepository.save(any(PortfolioEntity.class))).thenReturn(portfolioEntity);
        PortfolioResponse response = portfolioService.updatePortfolio(updateRequest);
        assertEquals(HttpStatus.OK.value(), response.getCode());
        assertEquals(ApplicationConstants.SUCCESS, response.getStatus());
        assertEquals(ApplicationConstants.SUCCESS_DATA_UPDATED_MSG, response.getMessage());
        assertNull(response.getData());
        verify(portfolioRepository, times(1)).findById(anyLong());
        verify(portfolioRepository, times(1)).save(any(PortfolioEntity.class));
        assertEquals(updateRequest.getName(), portfolioEntity.getName());
        assertEquals(updateRequest.getDescription(), portfolioEntity.getDescription());
        assertNotNull(portfolioEntity.getUpdatedAt());
    }

    @Test
    void updatePortfolio_UpdateOnlyName_ReturnsOkResponse() {
        PortfolioRequest updateRequest = new PortfolioRequest();
        updateRequest.setPortfolioId(1L);
        updateRequest.setName("Only Name Updated");
        updateRequest.setDescription(null);
        String originalDescription = portfolioEntity.getDescription();
        when(portfolioRepository.findById(anyLong())).thenReturn(Optional.of(portfolioEntity));
        when(portfolioRepository.save(any(PortfolioEntity.class))).thenReturn(portfolioEntity);
        PortfolioResponse response = portfolioService.updatePortfolio(updateRequest);
        assertEquals(HttpStatus.OK.value(), response.getCode());
        assertEquals(ApplicationConstants.SUCCESS, response.getStatus());
        assertEquals(ApplicationConstants.SUCCESS_DATA_UPDATED_MSG, response.getMessage());
        assertNull(response.getData());
        verify(portfolioRepository, times(1)).findById(anyLong());
        verify(portfolioRepository, times(1)).save(any(PortfolioEntity.class));
        assertEquals(updateRequest.getName(), portfolioEntity.getName());
        assertEquals(originalDescription, portfolioEntity.getDescription());
        assertNotNull(portfolioEntity.getUpdatedAt());
    }

    @Test
    void updatePortfolio_UpdateOnlyDescription_ReturnsOkResponse() {
        PortfolioRequest updateRequest = new PortfolioRequest();
        updateRequest.setPortfolioId(1L);
        updateRequest.setName(null);
        updateRequest.setDescription("Only Description Updated");
        String originalName = portfolioEntity.getName();
        when(portfolioRepository.findById(anyLong())).thenReturn(Optional.of(portfolioEntity));
        when(portfolioRepository.save(any(PortfolioEntity.class))).thenReturn(portfolioEntity);
        PortfolioResponse response = portfolioService.updatePortfolio(updateRequest);
        assertEquals(HttpStatus.OK.value(), response.getCode());
        assertEquals(ApplicationConstants.SUCCESS, response.getStatus());
        assertEquals(ApplicationConstants.SUCCESS_DATA_UPDATED_MSG, response.getMessage());
        assertNull(response.getData());
        verify(portfolioRepository, times(1)).findById(anyLong());
        verify(portfolioRepository, times(1)).save(any(PortfolioEntity.class));
        assertEquals(originalName, portfolioEntity.getName());
        assertEquals(updateRequest.getDescription(), portfolioEntity.getDescription());
        assertNotNull(portfolioEntity.getUpdatedAt());
    }

    @Test
    void deletePortfolio_PortfolioNotFound_ReturnsNotFoundResponse() {
        when(portfolioRepository.findById(anyLong())).thenReturn(Optional.empty());
        PortfolioResponse response = portfolioService.deletePortfolio(1L);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getCode());
        assertEquals(HttpStatus.NOT_FOUND.name(), response.getStatus());
        assertEquals(ApplicationConstants.NOT_FOUND_MSG, response.getMessage());
        assertNull(response.getData());
        verify(portfolioRepository, times(1)).findById(anyLong());
        verify(portfolioRepository, never()).deleteById(anyLong());
    }

    @Test
    void deletePortfolio_SuccessfulDeletion_ReturnsOkResponse() {
        when(portfolioRepository.findById(anyLong())).thenReturn(Optional.of(portfolioEntity));
        doNothing().when(portfolioRepository).deleteById(anyLong());
        PortfolioResponse response = portfolioService.deletePortfolio(1L);
        assertEquals(HttpStatus.OK.value(), response.getCode());
        assertEquals(ApplicationConstants.SUCCESS, response.getStatus());
        assertEquals(ApplicationConstants.SUCCESS_DATA_DELETED_MSG, response.getMessage());
        assertNull(response.getData());
        verify(portfolioRepository, times(1)).findById(anyLong());
        verify(portfolioRepository, times(1)).deleteById(anyLong());
    }
}