package com.wallex.dto;

import java.time.LocalDate;

public record TransactionRequest(
        double amount,
        String merchant,
        String category,
        LocalDate transactionDate,
        String description,
        String type
) {
}