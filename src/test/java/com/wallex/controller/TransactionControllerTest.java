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

import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;

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


        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.merchant").value("Uber Eats"))
            .andExpect(jsonPath("$.category").value("Food"))
            .andExpect(jsonPath("$.type").value("EXPENSE"));


}

    @Test
    void shouldGetAllTransactions() throws Exception{
        transactionRepository.deleteAll();
        TransactionRequest request = new TransactionRequest(
            42.00,
                "Walmart",
                "Groceries",
                LocalDate.of(2026, 5, 30),
                "Weekly groceries",
                TransactionType.EXPENSE
        );

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isCreated());

        mockMvc.perform(get("/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].merchant").value("Walmart"));
    }

    @Test 
    void shouldReturnBadRequestForInvalidTransaction() throws Exception{
        String invalidJson = """
        {
                 "amount": -10,
                  "merchant": "",
                  "category": "",
                  "transactionDate": null,
                  "description": "bad data",
                  "type": null

        }
        """;
        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }
    @Test
    void shouldUpdateTransaction() throws Exception {
        transactionRepository.deleteAll();

        TransactionRequest originalRequest = new TransactionRequest(
                15.99,
                "Uber Eats",
                "Food",
                LocalDate.of(2026, 5, 29),
                "Dinner order",
                TransactionType.EXPENSE
        );

        String responseBody = mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(originalRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = objectMapper.readTree(responseBody).get("id").asLong();

        TransactionRequest updateRequest = new TransactionRequest(
                22.50,
                "Uber Eats",
                "Food",
                LocalDate.of(2026, 5, 29),
                "Updated dinner order",
                TransactionType.EXPENSE
        );

        mockMvc.perform(put("/transactions/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(22.50))
                .andExpect(jsonPath("$.description").value("Updated dinner order"));
    }

    @Test
    void shouldDeleteTransaction() throws Exception {
        transactionRepository.deleteAll();

        TransactionRequest request = new TransactionRequest(
                15.99,
                "Uber Eats",
                "Food",
                LocalDate.of(2026, 5, 29),
                "Dinner order",
                TransactionType.EXPENSE
        );

        String responseBody = mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = objectMapper.readTree(responseBody).get("id").asLong();

        mockMvc.perform(delete("/transactions/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}




