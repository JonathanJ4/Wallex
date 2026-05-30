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
    public TransactionResponse getTransactionById(Long id){
        Transaction transaction = transactionRepository.findById(id)
        .orElseThrow(()-> new RuntimeException("Transaction not found with id:" +id));

    return mapToResponse(transaction);
    }

    public TransactionResponse updateTransaction(Long id, TransactionRequest request){
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id));

        transaction.setAmount(request.amount());
        transaction.setMerchant(request.merchant());
        transaction.setCategory(request.category());
        transaction.setTransactionDate(request.transactionDate());
        transaction.setDescription(request.description());
        transaction.setType(request.type());

        Transaction updatedTransaction = transactionRepository.save(transaction);

        return mapToResponse(updatedTransaction); 
    }

    public void deleteTransaction(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new RuntimeException("Transaction not found with id: " + id);
        }
        transactionRepository.deleteById(id);
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