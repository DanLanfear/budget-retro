package com.dlanfear.budget_retro_api.model;

import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    private String description;
    private Double amount;
    private String date;
    private String category;
}
