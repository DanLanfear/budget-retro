package com.dlanfear.budget_retro_api.model;

import com.google.cloud.Timestamp;
import lombok.*;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

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

    /**
     * Convert the date and returns the timestamp for the firestore
     *
     * @return firestore Timestamp obj
     */
    public Timestamp getTimestamp() {
        if (this.date == null) return null;

        ZonedDateTime utcMidnight = this.date.atStartOfDay(ZoneOffset.UTC);

        return Timestamp.ofTimeSecondsAndNanos(
                utcMidnight.toEpochSecond(),
                utcMidnight.getNano()
        );
    }
}
