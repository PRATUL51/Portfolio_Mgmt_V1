package com.java.portfolio.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankAccount {
    private Long bankAccountId;
    private Long portfolioId;
    private String bankName;
    private Long accountNo;
    private String accountType;
    private Double balance;
}
