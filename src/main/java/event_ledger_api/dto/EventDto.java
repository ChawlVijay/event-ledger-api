package event_ledger_api.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {

    @Id
    @NotBlank(message = "Event ID is required")
    private String eventId;

    @NotBlank(message = "Account ID is required")
    private String accountId;

    @Pattern(
            regexp = "(?i)CREDIT|DEBIT",
            message = "Type must be either CREDIT or DEBIT"
    )
    private String type;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than zero")
    private BigDecimal amount;

    @NotBlank(message = "Currency is required")
    private String currency;

    @NotNull(message = "Event timestamp is required")
    private Instant eventTimestamp;

    @NotBlank(message = "Metadata is required")
    private String metadata;

}