package com.wallex.service;

import com.wallex.dto.TransactionRequest;
import com.wallex.dto.TransactionResponse;
import com.wallex.entity.Transaction;
import com.wallex.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service 
public class TransactionService{
    private final TransactionRepository transactionRepository;

    public TransactionService( TransactionRepository transactionRepository){
        this.transactionRepository = transactionRepository;
        
    }

    public TransactionResponse createTransaction(TransactionRequest request){
        Transaction transaction = new Transaction(
            request.amount(),
                request.merchant(),
                request.category(),
                request.transactionDate(),
                request.description(),
                request.type()
        );
        Transaction savedTransaction = transactionRepository.save(transaction);
        return mapToResponse(savedTransaction);

    }

    public List<TransactionResponse> getAllTransactions() {
        return transactionRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
    private TransactionResponse mapToResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getMerchant(),
                transaction.getCategory(),
                transaction.getTransactionDate(),
                transaction.getDescription(),
                transaction.getType()
        );
    }

}