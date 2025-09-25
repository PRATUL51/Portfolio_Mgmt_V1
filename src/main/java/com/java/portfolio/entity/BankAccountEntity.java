package com.java.portfolio.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "bank_account")
@Table(name = "bank_account")

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankAccountEntity {
    @Id
    @Column(name = "bank_account_id")
    private Long bankAccountId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "portfolio_id", referencedColumnName = "portfolio_id")
    private PortfolioEntity portfolio;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "account_number")
    private Long accountNo;

    @Column(name = "account_type")
    private String accountType;

    @Column(name = "balance")
    private Double balance;

}
