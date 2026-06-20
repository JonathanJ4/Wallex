package com.wallex.service;

import com.wallex.entity.Transaction;
import com.wallex.enums.TransactionType;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service 
public class PdfTransactionParserService{
        private static final Pattern TRANSACTION_PATTERN = Pattern.compile(
            "^(?:Mon|Tue|Wed|Thu|Fri|Sat|Sun),\\s+" +
            "([A-Z][a-z]{2}\\.\\s+\\d{1,2},\\s+\\d{4})\\s+" +
            "(.+?)\\s+" +
            "(?:Pending|Posted)\\s+" +
            "\\+?\\$([\\d,]+\\.\\d{2})$"
        );

        private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM. d, uuuu", Locale.ENGLISH);

         public List<Transaction> parseTransactions(String extractedText) {
        List<Transaction> transactions = new ArrayList<>();

        String normalizedText = normalizeText(extractedText);

        for (String rawLine : normalizedText.split("\n")) {
            String line = rawLine.trim();

            Matcher matcher = TRANSACTION_PATTERN.matcher(line);

            if (!matcher.matches()) {
                continue;
            }

            LocalDate date = LocalDate.parse(
                    matcher.group(1),
                    DATE_FORMATTER
            );

            String merchant = matcher.group(2).trim();

            double amount = Double.parseDouble(
                    matcher.group(3).replace(",", "")
            );

            Transaction transaction = new Transaction(
                    amount,
                    merchant,
                    "Other",
                    date,
                    "Imported from PDF",
                    TransactionType.EXPENSE
            );

            transactions.add(transaction);
        }

        return transactions;
    }

    private String normalizeText(String text) {
        return text
                .replace("\r\n", "\n")
                .replace("\r", "\n")
                .replaceAll(
                        "(Mon|Tue|Wed|Thu|Fri|Sat|Sun),\\s+" +
                        "([A-Z][a-z]{2})\\.\\s*\\n\\s*" +
                        "(\\d{1,2},\\s+\\d{4})",
                        "$1, $2. $3"
                );
    }
}