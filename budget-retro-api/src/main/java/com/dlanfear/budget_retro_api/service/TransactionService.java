package com.dlanfear.budget_retro_api.service;

import com.dlanfear.budget_retro_api.model.card.TransactionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TransactionService {

    public void processTransactions(List<TransactionDTO> transactions) {
        log.info("Processing {} transactions", transactions.size());
        // save to database 100 at a time
    }
}
