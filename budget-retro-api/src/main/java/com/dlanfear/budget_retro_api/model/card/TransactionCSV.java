package com.dlanfear.budget_retro_api.model.card;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonPropertyOrder({"date", "description", "amount", "category"})
public class TransactionCSV {
    private String date;
    private String description;
    private Double amount;
    private String category;
}
