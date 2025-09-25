package com.java.portfolio.response;

import com.java.portfolio.model.BankAccount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankAccountResponse {
    int code;
    String status;
    String message;
    List<BankAccount> data;
}
