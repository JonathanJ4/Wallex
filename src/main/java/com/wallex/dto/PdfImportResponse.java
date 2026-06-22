package com.wallex.dto;
import com.wallex.entity.Transaction;
import java.util.*;

public record PdfImportResponse(
        List<Transaction> imported
) {
}