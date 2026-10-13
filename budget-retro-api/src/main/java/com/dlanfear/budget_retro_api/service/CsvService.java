package com.dlanfear.budget_retro_api.service;

import com.dlanfear.budget_retro_api.model.Transaction;
import com.dlanfear.budget_retro_api.model.card.TransactionCSV;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class CsvService {

    private final TransactionService transactionService;

    @Autowired
    public CsvService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public List<Transaction> processTransactions(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            CsvMapper csvMapper = new CsvMapper();

            CsvSchema csvSchema = csvMapper.schemaFor(TransactionCSV.class).withHeader().withColumnReordering(true);

            MappingIterator<TransactionCSV> mappingIterator = csvMapper.readerFor(TransactionCSV.class).with(csvSchema).readValues(inputStream);

            List<TransactionCSV> transactionInput = mappingIterator.readAll();
            return transactionService.processTransactions(transactionInput);
        } catch (IOException e) {
            System.out.println("Error processing file: " + e.getMessage());
            throw e;
        }
    }
}
