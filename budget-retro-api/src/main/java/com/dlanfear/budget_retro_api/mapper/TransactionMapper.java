package com.dlanfear.budget_retro_api.mapper;

import com.dlanfear.budget_retro_api.model.Transaction;
import com.dlanfear.budget_retro_api.model.csv.TransactionCsvRecord;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
public class TransactionMapper {

    /**
     * Converts an object from a CSV record to a domain object
     *
     * @param transactionCSV The CSV record
     * @return A Transaction object
     */
    public Transaction toTransaction(TransactionCsvRecord transactionCSV) {
        if (transactionCSV == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDate date = LocalDate.parse(transactionCSV.getDate(), formatter);
        return Transaction.builder()
                .date(date)
                .description(transactionCSV.getDescription().trim())
                .amount(transactionCSV.getAmount())
                .category(transactionCSV.getCategory().trim())
                .build();
    }

    /**
     * Converts a firestore document to a Transaction object
     *
     * @param document The firestore object
     * @return A transaction object with the document data
     */
    public Transaction toTransaction(QueryDocumentSnapshot document) {
        Transaction transaction = new Transaction();
        transaction.setId(document.getId());
        transaction.setDescription(document.getString("description"));
        transaction.setAmount(document.getDouble("amount"));
        transaction.setDate(parseDate(document.getTimestamp("date")));
        transaction.setCategory(document.getString("category"));
        return transaction;
    }

    /**
     * Turns a transaction object into a document for the firestore
     *
     * @param transaction The transaction object for the domain
     * @return QueryDocumentSnapshot of the transaction object
     */
    public QueryDocumentSnapshot toDocument(Transaction transaction) {
        return null;
    }

    /**
     * Parses a firestore timestamp to a LocalDate object
     *
     * @param timestamp The firestore timestamp from the document
     * @return The LocalDate object of the timestamp, or null if the timestamp is null
     */
    private static LocalDate parseDate(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        return timestamp.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
