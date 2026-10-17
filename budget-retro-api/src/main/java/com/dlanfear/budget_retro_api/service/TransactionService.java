package com.dlanfear.budget_retro_api.service;

import com.dlanfear.budget_retro_api.mapper.TransactionMapper;
import com.dlanfear.budget_retro_api.model.Transaction;
import com.dlanfear.budget_retro_api.repository.TransactionRepository;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    /**
     * Saves all transactions to the database
     *
     * @param transactions The list of transactions to save
     * @return The saved transactions
     */
    public List<Transaction> saveTransactions(List<Transaction> transactions) {
        log.info("Processing {} transactions", transactions.size());
        return this.transactionRepository.saveTransactions(transactions);
    }

    /**
     * Retrieves all transactions in the database
     *
     * @return All transactions in the database
     */
    public List<Transaction> getAllTransactions() {
        log.debug("Fetching all transactions from the repository");
        try {
            List<QueryDocumentSnapshot> documents = this.transactionRepository.getAllTransactions();
            List<Transaction> transactions = new ArrayList<>();
            for (QueryDocumentSnapshot document : documents) {
                transactions.add(this.transactionMapper.toTransaction(document));
            }
            return transactions;
        } catch (Exception e) {
            log.error("Error occurred while fetching all transactions", e);
            throw new RuntimeException("Error occurred while fetching all transactions", e);
        }
    }
}
