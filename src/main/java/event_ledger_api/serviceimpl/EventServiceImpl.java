package event_ledger_api.serviceimpl;

import event_ledger_api.entity.Event;
import event_ledger_api.service.EventService;

import java.math.BigDecimal;
import java.util.List;

public class EventServiceImpl implements EventService{

    @Override
    public Event saveEvent(Event event) {
        return null;
    }

    @Override
    public Event fetchEventById(String eventId) {
        return null;
    }

    @Override
    public List<Event> fetchEventsWithAccountId(String accountId) {
        return List.of();
    }

    @Override
    public BigDecimal fetchBalanceForAccountId() {
        return null;
    }

}
