package com.dlanfear.budget_retro_api.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
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
}
