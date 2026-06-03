package com.wallex.dto;
import com.wallex.enums.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record TransactionRequest(
        @Positive(message = "Amount must be greater than 0")
        double amount,

        @NotBlank(message = "Merchant is required")
        String merchant,

        @NotBlank(message = "Category is required")
        String category,

        @NotNull(message = "Transaction date is required")
        LocalDate transactionDate,

        String description,

        @NotNull(message = "Transaction type is required")
        TransactionType type
) {
}