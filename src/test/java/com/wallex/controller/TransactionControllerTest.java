package com.wallex.controller;

import com.wallex.dto.TransactionRequest;
import com.wallex.enums.TransactionType;
import com.wallex.repository.TransactionRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import org.springframework.test.context.ActiveProfiles;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TransactionControllerTest{
    @Autowired
    private MockMvc mockMvc;

    @Autowired 
    private ObjectMapper objectMapper;

    @Autowired 
    private TransactionRepository transactionRepository;

    @Test 
    void shouldCreateTransaction() throws Exception{    
        transactionRepository.deleteAll();
        TransactionRequest request = new TransactionRequest(
            15.99,
            "Uber Eats",
            "Food",
            LocalDate.of(2026, 5, 29),
            "Dinner order",
            TransactionType.EXPENSE
        );


        mockMvc.perform("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAString(request))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.merchant").value("Uber Eats"))
            .andExpect(jsonPath("$.category").value("Food"))
            .andExpect(jsonPath("$.type").value("EXPENSE"));


}
}