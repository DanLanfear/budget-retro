package com.dlanfear.budget_retro_api.repository;

import com.dlanfear.budget_retro_api.model.Transaction;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class TransactionRepository
{
    private final Firestore firestore;

    @Autowired
    public TransactionRepository(Firestore firestore) {
        this.firestore = firestore;
    }

    private CollectionReference getTransactionsCollection() {
        return firestore.collection("transactions");
    }

    public List<Transaction> getAllTransactions() throws ExecutionException, InterruptedException {
        CollectionReference transRef = getTransactionsCollection();
        ApiFuture<QuerySnapshot> future = transRef.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        List<Transaction> transactions = new ArrayList<>();

        for (QueryDocumentSnapshot document: documents) {
            transactions.add(document.toObject(Transaction.class));
        }
        return transactions;
    }
}
