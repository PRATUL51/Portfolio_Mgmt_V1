package com.java.portfolio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.portfolio.request.PortfolioRequest;
import com.java.portfolio.response.PortfolioResponse;
import com.java.portfolio.service.PortfolioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PortfolioController.class)
class PortfolioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PortfolioService portfolioService;

    @Autowired
    private ObjectMapper objectMapper;

    private PortfolioResponse mockPortfolioResponse;
    private PortfolioRequest mockPortfolioRequest;

    @BeforeEach
    void setUp() {
        // Initialize mock objects for consistent testing
        mockPortfolioResponse = new PortfolioResponse();
        mockPortfolioResponse.setMessage("Success");
        mockPortfolioResponse.setStatus("true");

        mockPortfolioRequest = new PortfolioRequest();
        // Add some dummy data to the request if needed for serialization/deserialization
        // For example: mockPortfolioRequest.setName("Test Portfolio");
    }

    @Test
    void testViewAll() throws Exception {
        when(portfolioService.viewAllPortfolio()).thenReturn(mockPortfolioResponse);

        mockMvc.perform(get("/api/portfolio/test"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mockPortfolioResponse)));
    }

    @Test
    void testAddPortfolio() throws Exception {
        when(portfolioService.addPortfolio(any(PortfolioRequest.class))).thenReturn(mockPortfolioResponse);

        mockMvc.perform(post("/api/portfolio")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockPortfolioRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mockPortfolioResponse)));
    }

    @Test
    void testViewById() throws Exception {
        Long portfolioId = 1L;
        when(portfolioService.viewPortfolioById(eq(portfolioId))).thenReturn(mockPortfolioResponse);

        mockMvc.perform(get("/api/portfolio/{id}", portfolioId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mockPortfolioResponse)));
    }

    @Test
    void testUpdatePortfolio() throws Exception {
        when(portfolioService.updatePortfolio(any(PortfolioRequest.class))).thenReturn(mockPortfolioResponse);

        mockMvc.perform(patch("/api/portfolio/test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockPortfolioRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mockPortfolioResponse)));
    }

    @Test
    void testDeletePortfolioById() throws Exception {
        Long portfolioId = 1L;
        when(portfolioService.deletePortfolio(eq(portfolioId))).thenReturn(mockPortfolioResponse);

        mockMvc.perform(delete("/api/portfolio/{id}/test", portfolioId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mockPortfolioResponse)));
    }
}