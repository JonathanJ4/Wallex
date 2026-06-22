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
public class PdfTransactionParserService {

    private static final Pattern TRANSACTION_START = Pattern.compile(
            "^(?:Mon|Tue|Wed|Thu|Fri|Sat|Sun),\\s*" +
            "([A-Z][a-z]{2})\\.?\\s+(\\d{1,2}),\\s+(\\d{4})(?:\\s+(.*))?$",
            Pattern.CASE_INSENSITIVE
    );

    private static final Pattern STATUS_PATTERN = Pattern.compile(
            "\\b(?:Pending|Posted)\\b",
            Pattern.CASE_INSENSITIVE
    );

    private static final Pattern AMOUNT_PATTERN = Pattern.compile(
            "[+-]?\\$[\\d,]+\\.\\d{2}"
    );

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("MMM d uuuu", Locale.ENGLISH);

    public List<Transaction> parseTransactions(String extractedText) {
        List<Transaction> transactions = new ArrayList<>();

        String normalizedText = normalizeText(extractedText);
        String[] lines = normalizedText.split("\n");

        int index = 0;
        while (index < lines.length) {
            String line = lines[index].trim();

            if (!isTransactionStart(line)) {
                index++;
                continue;
            }

            List<String> transactionLines = new ArrayList<>();
            transactionLines.add(line);
            index++;

            while (index < lines.length) {
                String nextLine = lines[index].trim();

                if (isTransactionStart(nextLine)) {
                    break;
                }

                if (isTransactionDetail(nextLine)) {
                    transactionLines.add(nextLine);
                }

                index++;
            }

            parseTransaction(transactionLines, transactions);
        }

        return transactions;
    }

    private void parseTransaction(
            List<String> transactionLines,
            List<Transaction> transactions
    ) {
        Matcher dateMatcher = TRANSACTION_START.matcher(transactionLines.get(0));

        if (!dateMatcher.matches()) {
            return;
        }

        String dateText =
                dateMatcher.group(1) + " " +
                dateMatcher.group(2) + " " +
                dateMatcher.group(3);

        LocalDate transactionDate = LocalDate.parse(dateText, DATE_FORMATTER);

        List<String> details = new ArrayList<>();

        if (dateMatcher.group(4) != null && !dateMatcher.group(4).isBlank()) {
            details.add(dateMatcher.group(4).trim());
        }

        for (int index = 1; index < transactionLines.size(); index++) {
            details.add(transactionLines.get(index));
        }

        String detailText = String.join(" ", details)
                .replaceAll("\\s+", " ")
                .trim();

        Matcher statusMatcher = STATUS_PATTERN.matcher(detailText);
        Matcher amountMatcher = AMOUNT_PATTERN.matcher(detailText);

        if (!statusMatcher.find() || !amountMatcher.find()) {
            return;
        }

        String amountText = amountMatcher.group();
        String merchant = detailText
                .replaceFirst("(?i)\\b(?:Pending|Posted)\\b", " ")
                .replace(amountText, " ")
                .replaceAll("\\s+", " ")
                .trim();

        if (merchant.isBlank()) {
            return;
        }

        double amount = Double.parseDouble(
                amountText
                        .replace("$", "")
                        .replace("+", "")
                        .replace(",", "")
        );

        transactions.add(new Transaction(
                Math.abs(amount),
                merchant,
                "Other",
                transactionDate,
                "Imported from PDF",
                TransactionType.EXPENSE
        ));
    }

    private boolean isTransactionStart(String line) {
        return TRANSACTION_START.matcher(line).matches();
    }

    private boolean isTransactionDetail(String line) {
        if (line.isBlank()) {
            return false;
        }

        return !line.equalsIgnoreCase("Date Description Status Debits Credits")
                && !line.equalsIgnoreCase("Date")
                && !line.equalsIgnoreCase("Description")
                && !line.equalsIgnoreCase("Status")
                && !line.equalsIgnoreCase("Debits")
                && !line.equalsIgnoreCase("Credits")
                && !line.equalsIgnoreCase("Transactions")
                && !line.equalsIgnoreCase("Filters")
                && !line.equalsIgnoreCase("Current statement period")
                && !line.equalsIgnoreCase("Current and last statement period")
                && !line.startsWith("Page created")
                && !line.startsWith("Current balance")
                && !line.startsWith("Available balance")
                && !line.contains("Account Details | Scotiabank");
    }

    private String normalizeText(String text) {
        return text
                .replace("\r\n", "\n")
                .replace("\r", "\n")
                .replace('\u00A0', ' ')
                .replaceAll("[ \\t]+", " ")

                /*
                 * Repairs dates split across lines:
                 *
                 * Thu, Jun.
                 * 18, 2026 Rogers...
                 */
                .replaceAll(
                        "(?i)(Mon|Tue|Wed|Thu|Fri|Sat|Sun),\\s*" +
                        "([A-Z][a-z]{2})\\.?\\s*\\n\\s*" +
                        "(\\d{1,2},\\s*\\d{4})",
                        "$1, $2. $3"
                );
    }
}
