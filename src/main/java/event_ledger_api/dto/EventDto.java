package event_ledger_api.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {

    @NotEmpty
    private String accountId;

    @NotEmpty
    private String type;

    @NotEmpty
    private BigDecimal amount;

    @NotEmpty
    private String currency;

    @NotEmpty
    private Instant eventTimestamp;

    @NotEmpty
    private String metadata;

}
