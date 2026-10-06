package event_ledger_api.dto;

import lombok.Data;

@Data
public class EventSaveResult {

    private final EventResponse event;
    private final boolean duplicate;

    public EventSaveResult(EventResponse event, boolean duplicate) {
        this.event = event;
        this.duplicate = duplicate;
    }

}