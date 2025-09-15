package com.java.portfolio.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CustomerRequest {
    private String panId;
    private String firstName;
    private String lastName;
    private String email;
    private Long phoneNo;
    private LocalDate birthDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
