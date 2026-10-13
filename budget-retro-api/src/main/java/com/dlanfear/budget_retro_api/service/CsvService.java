package com.dlanfear.budget_retro_api.service;

import com.dlanfear.budget_retro_api.model.Transaction;
import com.dlanfear.budget_retro_api.model.card.TransactionCSV;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CsvService {

    @Autowired
    public CsvService() {
    }

    public List<Transaction> processTransactions(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            CsvMapper csvMapper = new CsvMapper();
            CsvSchema csvSchema = csvMapper.schemaFor(TransactionCSV.class).withHeader().withColumnReordering(true);
            MappingIterator<TransactionCSV> mappingIterator = csvMapper.readerFor(TransactionCSV.class).with(csvSchema).readValues(inputStream);
            List<TransactionCSV> csvRows = mappingIterator.readAll();

            //todo map from transactionCSV to Transaction
            return new ArrayList<>();
        } catch (IOException e) {
            System.out.println("Error processing file: " + e.getMessage());
            throw e;
        }
    }
}
