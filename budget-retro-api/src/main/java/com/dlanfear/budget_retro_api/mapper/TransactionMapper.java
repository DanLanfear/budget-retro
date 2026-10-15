package com.dlanfear.budget_retro_api.mapper;

import com.dlanfear.budget_retro_api.model.Transaction;
import com.dlanfear.budget_retro_api.model.card.TransactionCSV;

import java.time.LocalDate;

public class TransactionMapper {

    /**
     * Converts an object from a CSV record to a domain object
     *
     * @param transactionCSV The CSV record
     * @return A Transaction object
     */
    public Transaction toTransaction(TransactionCSV transactionCSV) {
        if (transactionCSV == null) {
            return null;
        }

        LocalDate date = LocalDate.parse(transactionCSV.getDate());
        return Transaction.builder()
                .date(date)
                .description(transactionCSV.getDescription())
                .amount(transactionCSV.getAmount())
                .category(transactionCSV.getCategory())
                .build();
    }
}
