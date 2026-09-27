package com.dlanfear.budget_retro_api.controller;

import com.dlanfear.budget_retro_api.model.card.DefaultCard;
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
@CrossOrigin(origins = "http://localhost:4200")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/upload/transactions")
    public ResponseEntity<List<DefaultCard>> uploadTransactions(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        try (InputStream inputStream = file.getInputStream()) {
            CsvMapper csvMapper = new CsvMapper();

            CsvSchema csvSchema = csvMapper.schemaFor(DefaultCard.class).withHeader().withColumnReordering(true);

            MappingIterator<DefaultCard> mappingIterator = csvMapper.readerFor(DefaultCard.class).with(csvSchema).readValues(inputStream);

            List<DefaultCard> transactions = mappingIterator.readAll();
            transactionService.processTransactions(transactions);
            return ResponseEntity.ok(transactions);
        } catch (IOException e) {
            return ResponseEntity.status(500).build();
        }
    }
}
