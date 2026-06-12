package com.wallex.repository;

import com.wallex.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import com.wallex.enums.TransactionType;
import java.time.LocalDate;
import java.util.*;

public interface TransactionRepository extends JpaRepository<Transaction, Long>{

List<Transaction> findByCategoryIgnoreCase(String category);
 List<Transaction> findByType(TransactionType type);
List<Transaction> findByMerchantContainingIgnoreCase(String Merchant);
List<Transaction> findByTransactionDateBetween(LocalDate startDate, LocalDate endDate);

}