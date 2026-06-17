package com.wallex.service;

import com.wallex.dto.TransactionRequest;
import com.wallex.dto.TransactionResponse;
import com.wallex.entity.Transaction;
import com.wallex.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import com.wallex.exception.TransactionNotFoundException;
import java.time.LocalDate;
import com.wallex.enums.TransactionType;

import java.util.List;
import com.opencsv.CSVReader;
import com.wallex.dto.ImportSummaryResponse;
import com.wallex.enums.TransactionType;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.time.LocalDate;




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
        .orElseThrow(()-> new TransactionNotFoundException(id));

    return mapToResponse(transaction);
    }

    public TransactionResponse updateTransaction(Long id, TransactionRequest request){
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));

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
            throw new TransactionNotFoundException(id);
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
    public List<TransactionResponse> searchTransaction(
        String category,
        String merchant,
        TransactionType type,
        LocalDate startDate,
        LocalDate endDate 

    ){
        List<Transaction> transactions;

        if(category!= null){
            transactions= transactionRepository.findByCategoryIgnoreCase(category);

        }
        else if(merchant!= null){
            transactions= transactionRepository.findByMerchantContainingIgnoreCase(merchant);
        }
        else if (startDate != null && endDate != null) {
        transactions = transactionRepository.findByTransactionDateBetween(startDate, endDate);
         } else {
        transactions = transactionRepository.findAll();
        }

    return transactions.stream()
    .map(this::mapToResponse)
    .toList();
        
    }
    public ImportSummaryResponse importTransactionsFromCsv(MultipartFile file) {
    int imported = 0;
    int failed = 0;

    try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
        String[] row;

        // Skip header row
        csvReader.readNext();

        while ((row = csvReader.readNext()) != null) {
            try {
                double amount = Double.parseDouble(row[0]);
                String merchant = row[1];
                String category = row[2];
                LocalDate transactionDate = LocalDate.parse(row[3]);
                String description = row[4];
                TransactionType type = TransactionType.valueOf(row[5].toUpperCase());

                Transaction transaction = new Transaction(
                        amount,
                        merchant,
                        category,
                        transactionDate,
                        description,
                        type
                );

                transactionRepository.save(transaction);
                imported++;

            } catch (Exception rowException) {
                failed++;
            }
        }

        return new ImportSummaryResponse(imported, failed);

    } catch (Exception e) {
        throw new RuntimeException("Failed to import CSV file");
    }
}
}