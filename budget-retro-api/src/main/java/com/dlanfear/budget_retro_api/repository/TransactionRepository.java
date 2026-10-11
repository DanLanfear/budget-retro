package com.dlanfear.budget_retro_api.repository;

import com.dlanfear.budget_retro_api.model.Transaction;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class TransactionRepository {
    private final Firestore firestore;

    @Autowired
    public TransactionRepository(Firestore firestore) {
        this.firestore = firestore;
    }

    /**
     * Gets the transactions collection from firestore
     * @return The transactions collection reference
     */
    private CollectionReference getTransactionsCollection() {
        return firestore.collection("transactions");
    }

    /**
     * Fetches all the transactions
     *
     * @return A list of all the transactions
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public List<Transaction> getAllTransactions() throws ExecutionException, InterruptedException {
        CollectionReference transRef = getTransactionsCollection();
        ApiFuture<QuerySnapshot> future = transRef.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        List<Transaction> transactions = new ArrayList<>();

        for (QueryDocumentSnapshot document : documents) {
            transactions.add(documentToTransaction(document));
        }
        return transactions;
    }

    /**
     * Converts a firestore document to a Transaction object
     *
     * @param document The firestore object
     * @return A transaction object with the document data
     */
    private static Transaction documentToTransaction(QueryDocumentSnapshot document) {
        Transaction transaction = new Transaction();
        transaction.setId(document.getId());
        transaction.setDescription(document.getString("description"));
        transaction.setAmount(document.getDouble("amount"));
        transaction.setDate(parseDate(document.getTimestamp("date")));
        transaction.setCategory(document.getString("category"));
        return transaction;
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
