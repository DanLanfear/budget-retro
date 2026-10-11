package com.dlanfear.budget_retro_api.model;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction {
    private String id;
    private String description;
    private Double amount;
    private LocalDate date;
    private String category;
}
