package event_ledger_api.dto;

public class EventSaveResult {

    private final EventResponse event;
    private final boolean duplicate;

    public EventSaveResult(EventResponse event, boolean duplicate) {
        this.event = event;
        this.duplicate = duplicate;
    }

    public EventResponse getEvent() {
        return event;
    }

    public boolean isDuplicate() {
        return duplicate;
    }
}