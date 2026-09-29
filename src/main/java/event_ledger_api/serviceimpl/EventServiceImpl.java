package event_ledger_api.serviceimpl;

import event_ledger_api.dto.EventDto;
import event_ledger_api.dto.EventResponse;
import event_ledger_api.dto.EventSaveResult;
import event_ledger_api.entity.Event;
import event_ledger_api.repo.EventRepo;
import event_ledger_api.service.EventService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepo eventRepo;

    public EventServiceImpl(EventRepo eventRepo) {
        this.eventRepo = eventRepo;
    }

    @Override
    public EventSaveResult saveEvent(EventDto dto) {

        Event existingEvent = eventRepo.findById(dto.getEventId()).orElse(null);

        if (existingEvent != null) {
            return new EventSaveResult(mapToResponse(existingEvent),true);
        }

        Event event = new Event();
        event.setEventId(dto.getEventId());
        event.setAccountId(dto.getAccountId());
        event.setType(dto.getType().toUpperCase());
        event.setAmount(dto.getAmount());
        event.setCurrency(dto.getCurrency());
        event.setEventTimestamp(dto.getEventTimestamp());
        event.setMetadata(dto.getMetadata());

        Event saved = eventRepo.save(event);

        return new EventSaveResult(mapToResponse(saved),false);
    }

    public EventResponse mapToResponse(Event event)
    {
        return new EventResponse(
                event.getEventId(),
                event.getAccountId(),
                event.getType(),
                event.getAmount(),
                event.getCurrency(),
                event.getEventTimestamp(),
                event.getMetadata());
    }

    @Override
    public EventResponse fetchEventById(String eventId) {

        Event event = eventRepo.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));

        return mapToResponse(event);
    }

    @Override
    public List<EventResponse> fetchEventsWithAccountId(String accountId) {
        List<Event> eventResponseList = eventRepo.findByAccountIdOrderByEventTimestampAsc(accountId);
        return eventResponseList.stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BigDecimal fetchBalanceForAccountId(String accountId) {

        List<Event> eventResponseList = eventRepo.findByAccountIdOrderByEventTimestampAsc(accountId);

        BigDecimal balance = BigDecimal.ZERO;

        for(Event event : eventResponseList)
        {
            if(event.getType().equals("CREDIT"))
            {
                balance = balance.add(event.getAmount());
            }
            if(event.getType().equals("DEBIT")) {
                balance = balance.subtract(event.getAmount());
            }
        }

        return balance;

    }
}