package event_ledger_api.service;

import event_ledger_api.entity.Event;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public interface EventService {

    Event saveEvent(Event event);

    Event fetchEventById(String eventId);

    List<Event> fetchEventsWithAccountId(String accountId);

    BigDecimal fetchBalanceForAccountId();

}
