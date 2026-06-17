package com.wallex.controller;

import com.wallex.dto.TransactionRequest;
import com.wallex.dto.TransactionResponse;
import com.wallex.service.TransactionService;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import com.wallex.enums.TransactionType;

import com.wallex.dto.ImportSummaryResponse;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse createTransaction(@Valid @RequestBody TransactionRequest request) {
        return transactionService.createTransaction(request);
    }

    @GetMapping
    public List<TransactionResponse> getAllTransactions() {
        return transactionService.getAllTransactions();
    }
    @GetMapping("/{id}")
    public TransactionResponse getTransactionById(@PathVariable Long id) {
        return transactionService.getTransactionById(id);
    }

    @PutMapping("/{id}")
    public TransactionResponse updateTransaction(
            @PathVariable Long id,
            @Valid @RequestBody TransactionRequest request
    ) {
        return transactionService.updateTransaction(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
    }

    @GetMapping("/search")
public List<TransactionResponse> searchTransaction(
    @RequestParam(required=false) String category,
    @RequestParam(required=false) String merchant, 
    @RequestParam(required = false) TransactionType type,
    @RequestParam(required = false) LocalDate startDate,
    @RequestParam(required = false) LocalDate endDate
){
    
    return transactionService.searchTransaction(category, merchant, type, startDate, endDate);

}

@PostMapping(value = "/import/csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ImportSummaryResponse importTransactionsFromCsv(@RequestParam("file") MultipartFile file) {
    return transactionService.importTransactionsFromCsv(file);
}
}