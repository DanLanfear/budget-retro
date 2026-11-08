package com.dlanfear.budget_retro_api.model.documents;

import com.google.cloud.Timestamp;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionDocument {
    private String id;
    private String description;
    private Double amount;
    private Timestamp date;
    private String category;
}
