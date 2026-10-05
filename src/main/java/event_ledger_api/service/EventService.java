package event_ledger_api.service;

import event_ledger_api.dto.EventDto;
import event_ledger_api.dto.EventResponse;
import event_ledger_api.dto.EventSaveResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface EventService {

    EventSaveResult saveEvent(EventDto event);

    EventResponse fetchEventById(String eventId);

    Page<EventResponse> fetchEventsByAccount(String accountId, Pageable pageable);

    BigDecimal fetchBalanceForAccountId(String accountId);

}
