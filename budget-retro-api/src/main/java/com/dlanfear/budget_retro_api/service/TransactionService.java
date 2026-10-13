package com.dlanfear.budget_retro_api.service;

import com.dlanfear.budget_retro_api.model.Transaction;
import com.dlanfear.budget_retro_api.model.card.TransactionCSV;
import com.dlanfear.budget_retro_api.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TransactionService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> processTransactions(List<TransactionCSV> transactions) {
        log.info("Processing {} transactions", transactions.size());
        List<Transaction> transactionOutput = new ArrayList<>();
        for (TransactionCSV entry : transactions) {
            Transaction transaction = Transaction.builder()
                    .description(entry.getDescription())
                    .amount(entry.getAmount())
                    .date(LocalDate.parse(entry.getDate()))
                    .category(entry.getCategory())
                    .build();
//            transactionRepository.saveTransaction(transaction);
            transactionOutput.add(transaction);
        }
        // save to database 100 at a time
        return transactionOutput;
    }

    /**
     * Retrieves all transactions in the database
     * @return All transactions in the database
     */
    public List<Transaction> getAllTransactions() {
        log.debug("Fetching all transactions from the repository");
        try {
            return this.transactionRepository.getAllTransactions();
        } catch (Exception e) {
            log.error("Error occurred while fetching all transactions", e);
            throw new RuntimeException("Error occurred while fetching all transactions", e);
        }
    }
}
