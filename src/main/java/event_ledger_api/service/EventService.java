package event_ledger_api.service;

import event_ledger_api.dto.EventDto;
import event_ledger_api.dto.EventResponse;
import event_ledger_api.dto.EventSaveResult;

import java.math.BigDecimal;
import java.util.List;

public interface EventService {

    EventSaveResult saveEvent(EventDto event);

    EventResponse fetchEventById(String eventId);

    List<EventResponse> fetchEventsWithAccountId(String accountId);

    BigDecimal fetchBalanceForAccountId(String accountId);

}
