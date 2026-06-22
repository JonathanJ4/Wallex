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

    /*
     * Captures:
     * group 1 = month
     * group 2 = day
     * group 3 = year
     * group 4 = everything between date and Pending/Posted
     * group 5 = amount
     */
    private static final Pattern TRANSACTION_PATTERN = Pattern.compile(
            "^(?:Mon|Tue|Wed|Thu|Fri|Sat|Sun),\\s*" +
            "([A-Z][a-z]{2})\\.?\\s+(\\d{1,2}),\\s+(\\d{4})\\s+" +
            "(.+?)\\s+" +
            "(?:Pending|Posted)\\s+" +
            "\\+?\\$([\\d,]+\\.\\d{2})$",
            Pattern.CASE_INSENSITIVE
    );

    private static final Pattern TRANSACTION_START = Pattern.compile(
            "^(?:Mon|Tue|Wed|Thu|Fri|Sat|Sun),",
            Pattern.CASE_INSENSITIVE
    );

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("MMM d uuuu", Locale.ENGLISH);

    public List<Transaction> parseTransactions(String extractedText) {
        List<Transaction> transactions = new ArrayList<>();

        String normalizedText = normalizeText(extractedText);
        String[] lines = normalizedText.split("\n");

        for (int index = 0; index < lines.length; index++) {
            String line = lines[index].trim();

            Matcher matcher = TRANSACTION_PATTERN.matcher(line);

            if (!matcher.matches()) {
                continue;
            }

            String dateText =
                    matcher.group(1) + " " +
                    matcher.group(2) + " " +
                    matcher.group(3);

            LocalDate transactionDate =
                    LocalDate.parse(dateText, DATE_FORMATTER);

            // Everything between the date and Pending/Posted
            String merchant = matcher.group(4)
                    .replaceAll("\\s+", " ")
                    .trim();

            /*
             * Append the immediate next line when it belongs
             * to the description column.
             */
            if (index + 1 < lines.length) {
                String nextLine = lines[index + 1].trim();

                if (isDescriptionContinuation(nextLine)) {
                    merchant += " " + nextLine;
                    index++;
                }
            }

            double amount = Double.parseDouble(
                    matcher.group(5).replace(",", "")
            );

            transactions.add(new Transaction(
                    amount,
                    merchant,
                    "Other",
                    transactionDate,
                    "Imported from PDF",
                    TransactionType.EXPENSE
            ));
        }

        return transactions;
    }

    private boolean isDescriptionContinuation(String line) {
        if (line.isBlank()) {
            return false;
        }

        // A new dated line is the next transaction.
        if (TRANSACTION_START.matcher(line).find()) {
            return false;
        }

        // Prevent page/header text from becoming part of a merchant.
        return !line.equalsIgnoreCase("Date Description Status Debits Credits")
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