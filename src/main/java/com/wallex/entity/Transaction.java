package com.wallex.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class Transaction{
        @Id
        @GeneratedValue( strategy= GenerationType.IDENTITY)
        private Long id;

        private double amount;

        private String merchant;

        private String category;

         private LocalDate transactionDate;

    private String description;

    private String type;

    public Transaction(
            double amount,
            String merchant,
            String category,
            LocalDate transactionDate,
            String description,
            String type
    ) {
        this.amount = amount;
        this.merchant = merchant;
        this.category = category;
        this.transactionDate = transactionDate;
        this.description = description;
        this.type = type;
    }
}


 