package com.dlanfear.budget_retro_api.controller;

import com.dlanfear.budget_retro_api.model.Transaction;
import com.dlanfear.budget_retro_api.service.CsvService;
import com.dlanfear.budget_retro_api.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/transactions")
//@CrossOrigin(origins = "http://localhost:4200")
@CrossOrigin(origins = {"https://hoppscotch.io", "http://localhost:4200"}, allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})

public class TransactionController {

    private final TransactionService transactionService;
    private final CsvService csvService;

    @Autowired
    public TransactionController(TransactionService transactionService, CsvService csvService) {
        this.transactionService = transactionService;
        this.csvService = csvService;
    }

    @GetMapping()
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

    @PostMapping("/upload/transactions")
    public ResponseEntity<List<Transaction>> uploadTransactions(@RequestBody MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            List<Transaction> transactions = this.csvService.processTransactions(file);
            return ResponseEntity.ok(transactions);
        } catch (IOException e) {
            log.error("Error processing file: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }
}
