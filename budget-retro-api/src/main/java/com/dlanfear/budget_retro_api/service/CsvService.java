package com.dlanfear.budget_retro_api.service;

import com.dlanfear.budget_retro_api.mapper.TransactionMapper;
import com.dlanfear.budget_retro_api.model.Transaction;
import com.dlanfear.budget_retro_api.model.card.TransactionCsvRecord;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@Slf4j
public class CsvService {

    private final TransactionMapper transactionMapper;

    @Autowired
    public CsvService(TransactionMapper mapper) {
        this.transactionMapper = mapper;
    }

    /**
     * Processes a CSV file containing transaction data and maps it to a list of Transaction objects.
     *
     * @param file The file with the csv records
     * @return The list of transaction objects
     * @throws IOException if there is an error reading the file
     */
    public List<Transaction> processTransactions(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            CsvMapper csvMapper = new CsvMapper();
            CsvSchema csvSchema = csvMapper.schemaFor(TransactionCsvRecord.class).withHeader().withColumnReordering(true);
            MappingIterator<TransactionCsvRecord> mappingIterator = csvMapper.readerFor(TransactionCsvRecord.class).with(csvSchema).readValues(inputStream);
            List<TransactionCsvRecord> csvRows = mappingIterator.readAll();
            return csvRows.stream().map(transactionMapper::toTransaction).toList();
        } catch (IOException e) {
            System.out.println("Error processing file: " + e.getMessage());
            throw e;
        }
    }
}
