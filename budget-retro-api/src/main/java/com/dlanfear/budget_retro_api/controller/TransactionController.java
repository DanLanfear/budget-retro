package com.dlanfear.budget_retro_api.controller;

import com.dlanfear.budget_retro_api.model.Transaction;
import com.dlanfear.budget_retro_api.model.card.TransactionCSV;
import com.dlanfear.budget_retro_api.service.TransactionService;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
//@CrossOrigin(origins = "http://localhost:4200")
@CrossOrigin(origins = {"https://hoppscotch.io", "http://localhost:4200"}, allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})

public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping()
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

    @PostMapping("/upload/transactions")
    public ResponseEntity<List<Transaction>> uploadTransactions(@RequestBody MultipartFile file) {
        System.out.println("Received file: " + file.getOriginalFilename());
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        try (InputStream inputStream = file.getInputStream()) {
            CsvMapper csvMapper = new CsvMapper();

            CsvSchema csvSchema = csvMapper.schemaFor(TransactionCSV.class).withHeader().withColumnReordering(true);

            MappingIterator<TransactionCSV> mappingIterator = csvMapper.readerFor(TransactionCSV.class).with(csvSchema).readValues(inputStream);

            List<TransactionCSV> transactionInput = mappingIterator.readAll();
            List<Transaction> transactions = transactionService.processTransactions(transactionInput);
            return ResponseEntity.ok(transactions);
        } catch (IOException e) {
            System.out.println("Error processing file: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }
}
