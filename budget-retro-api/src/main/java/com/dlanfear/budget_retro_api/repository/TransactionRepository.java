package com.dlanfear.budget_retro_api.repository;

import com.dlanfear.budget_retro_api.model.Transaction;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
     *
     * @return The transactions collection reference
     */
    private CollectionReference getTransactionsCollection() {
        return firestore.collection("transactions");
    }

    /**
     * Fetches all the transactions as documents
     *
     * @return A list of documents for all transactions
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public List<QueryDocumentSnapshot> getAllTransactions() throws ExecutionException, InterruptedException {
        CollectionReference transRef = getTransactionsCollection();
        ApiFuture<QuerySnapshot> future = transRef.get();
        return future.get().getDocuments();
    }

    /**
     * Saves a list of transactions in a batch of 500 maximum
     *
     * @param transactions The list of transactions
     */
    public List<Transaction> saveTransactions(List<Transaction> transactions) {
        CollectionReference transRef = getTransactionsCollection();
        int totalSize = transactions.size();
        int batchLimit = 500;

        for (int i = 0; i < totalSize; i += batchLimit) {
            int endIndex = Math.min(i + batchLimit, totalSize);
            List<Transaction> chunk = transactions.subList(i, endIndex);

            WriteBatch batch = firestore.batch();

            for (Transaction transaction : chunk) {
                DocumentReference docRef;
                if (transaction.getId() != null && !transaction.getId().trim().isEmpty()) {
                    docRef = transRef.document(transaction.getId());
                } else {
                    docRef = transRef.document();
                    transaction.setId(docRef.getId());
                }
                batch.set(docRef, transaction);

                try {
                    batch.commit().get();

                } catch (Exception e) {
                    System.err.println("Batch write failed: " + e.getMessage());
                }
            }
        }
        return transactions;
    }
}
