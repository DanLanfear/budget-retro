package com.dlanfear.budget_retro_api.service;

import com.dlanfear.budget_retro_api.model.Transaction;
import com.dlanfear.budget_retro_api.model.card.DefaultCard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TransactionService {

    public List<Transaction> processTransactions(List<DefaultCard> transactions) {
        log.info("Processing {} transactions", transactions.size());
        return new ArrayList<>();
    }
}
