package com.dlanfear.budget_retro_api.model.card;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonPropertyOrder({"date", "description", "amount", "category"})
public class DefaultCard {
    private Date date;
    private String description;
    private Double amount;
    private String category;
}
