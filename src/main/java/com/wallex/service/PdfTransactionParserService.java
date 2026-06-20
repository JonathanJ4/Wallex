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

        private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("MMM. d, uuuu", Locale.ENGLISH);

}