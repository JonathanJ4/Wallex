package com.wallex.dto;

import com.wallex.enums.TransactionType;

import java.time.LocalDate;

public record TransactionResponse(
        Long id,
        double amount,
        String merchant,
        String category,
        LocalDate transactionDate,
        String description,
        TransactionType type
) {
}