package event_ledger_api.serviceimpl;

import event_ledger_api.dto.EventDto;
import event_ledger_api.dto.EventResponse;
import event_ledger_api.dto.EventSaveResult;
import event_ledger_api.entity.Event;
import event_ledger_api.repo.EventRepo;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    @Mock
    private EventRepo eventRepo;

    @InjectMocks
    private EventServiceImpl eventService;

    @Test
    void shouldSaveNewEvent() {

        EventDto dto = new EventDto(
                "EVT-001",
                "ACC-001",
                "CREDIT",
                new BigDecimal("5000"),
                "INR",
                Instant.parse("2026-09-23T10:00:00Z"),
                "mainframe"
        );

        Event savedEvent = new Event(
                "EVT-001",
                "ACC-001",
                "CREDIT",
                new BigDecimal("5000"),
                "INR",
                Instant.parse("2026-09-23T10:00:00Z"),
                "mainframe"
        );
        when(eventRepo.findById("EVT-001"))
                .thenReturn(Optional.empty());

        when(eventRepo.save(any(Event.class)))
                .thenReturn(savedEvent);
        EventSaveResult result = eventService.saveEvent(dto);
        assertFalse(result.isDuplicate());

        assertEquals(
                "EVT-001",
                result.getEvent().getEventId()
        );

        assertEquals(
                "ACC-001",
                result.getEvent().getAccountId()
        );

        assertEquals(
                "CREDIT",
                result.getEvent().getType()
        );

        assertEquals(
                new BigDecimal("5000"),
                result.getEvent().getAmount()
        );
        verify(eventRepo).findById("EVT-001");

        verify(eventRepo).save(any(Event.class));
    }

    @Test
    void shouldReturnExistingEventWhenDuplicateEventIsReceived() {

        Event existingEvent = new Event(
                "EVT-001",
                "ACC-001",
                "CREDIT",
                new BigDecimal("5000"),
                "INR",
                Instant.parse("2026-09-23T10:00:00Z"),
                "mainframe"
        );

        EventDto duplicateDto = new EventDto(
                "EVT-001",
                "ACC-001",
                "DEBIT",
                new BigDecimal("2000"),
                "INR",
                Instant.parse("2026-09-23T11:00:00Z"),
                "another-source"
        );

        when(eventRepo.findById("EVT-001"))
                .thenReturn(Optional.of(existingEvent));

        EventSaveResult result =
                eventService.saveEvent(duplicateDto);

        assertTrue(result.isDuplicate());

        assertEquals(
                "EVT-001",
                result.getEvent().getEventId()
        );

        assertEquals(
                "CREDIT",
                result.getEvent().getType()
        );

        assertEquals(
                new BigDecimal("5000"),
                result.getEvent().getAmount()
        );

        verify(eventRepo, never())
                .save(any(Event.class));

        verify(eventRepo)
                .findById("EVT-001");
    }

    @Test
    void shouldReturnExistingEventWhenConcurrentSaveCausesDataIntegrityViolation() {

        Event existingEvent = new Event(
                "EVT-001",
                "ACC-001",
                "CREDIT",
                new BigDecimal("5000"),
                "INR",
                Instant.parse("2026-09-23T10:00:00Z"),
                "mainframe"
        );

        EventDto dto = new EventDto(
                "EVT-001",
                "ACC-001",
                "CREDIT",
                new BigDecimal("5000"),
                "INR",
                Instant.parse("2026-09-23T10:00:00Z"),
                "mainframe"
        );
        when(eventRepo.findById("EVT-001"))
                .thenReturn(
                        Optional.empty(),
                        Optional.of(existingEvent)
                );

        when(eventRepo.save(any(Event.class)))
                .thenThrow(new DataIntegrityViolationException(
                        "Duplicate event ID"
                ));

        EventSaveResult result =
                eventService.saveEvent(dto);

        assertTrue(result.isDuplicate());
        assertEquals(
                "EVT-001",
                result.getEvent().getEventId()
        );

        assertEquals(
                new BigDecimal("5000"),
                result.getEvent().getAmount()
        );
        verify(eventRepo, times(2))
                .findById("EVT-001");

        verify(eventRepo)
                .save(any(Event.class));
    }

    @Test
    void shouldFetchEventById() {

        Event event = new Event(
                "EVT-001",
                "ACC-001",
                "CREDIT",
                new BigDecimal("5000"),
                "INR",
                Instant.parse("2026-09-23T10:00:00Z"),
                "mainframe"
        );

        when(eventRepo.findById("EVT-001"))
                .thenReturn(Optional.of(event));

        EventResponse response =
                eventService.fetchEventById("EVT-001");

        assertEquals(
                "EVT-001",
                response.getEventId()
        );

        assertEquals(
                "ACC-001",
                response.getAccountId()
        );

        assertEquals(
                "CREDIT",
                response.getType()
        );

        assertEquals(
                new BigDecimal("5000"),
                response.getAmount()
        );

        verify(eventRepo)
                .findById("EVT-001");
    }

    @Test
    void shouldThrowExceptionWhenEventDoesNotExist() {

        when(eventRepo.findById("EVT-999"))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> eventService.fetchEventById("EVT-999")
                );
        assertEquals(
                "Event not found with id: EVT-999",
                exception.getMessage()
        );
        verify(eventRepo)
                .findById("EVT-999");
    }

    @Test
    void shouldReturnPaginatedEventsForAccount() {

        List<Event> events = getEvents();

        Pageable pageable =
                PageRequest.of(
                        0,
                        2,
                        Sort.by("eventTimestamp").ascending()
                );

        Page<Event> eventPage =
                new PageImpl<>(
                        events,
                        pageable,
                        5
                );

        when(eventRepo.findByAccountIdOrderByEventTimestampAsc(
                "ACC-001",
                pageable
        )).thenReturn(eventPage);

        Page<EventResponse> result =
                eventService.fetchEventsByAccount(
                        "ACC-001",
                        pageable
                );
        assertEquals(2, result.getContent().size());

        assertEquals(
                "EVT-001",
                result.getContent()
                        .get(0)
                        .getEventId()
        );
        assertEquals("EVT-002", result.getContent()
                        .get(1)
                        .getEventId()
        );
        assertEquals(5, result.getTotalElements());
        verify(eventRepo)
                .findByAccountIdOrderByEventTimestampAsc("ACC-001", pageable);
    }

    private static @NonNull List<Event> getEvents() {
        Event event1 = new Event(
                "EVT-001",
                "ACC-001",
                "CREDIT",
                new BigDecimal("5000"),
                "INR",
                Instant.parse("2026-09-23T10:00:00Z"),
                "source"
        );

        Event event2 = new Event(
                "EVT-002",
                "ACC-001",
                "DEBIT",
                new BigDecimal("1000"),
                "INR",
                Instant.parse("2026-09-23T11:00:00Z"),
                "source"
        );

        return List.of(event1,event2);
    }

    @Test
    void shouldCalculateAccountBalance() {

        Event credit1 = new Event(
                "EVT-001",
                "ACC-001",
                "CREDIT",
                new BigDecimal("5000"),
                "INR",
                Instant.parse("2026-09-23T10:00:00Z"),
                "source"
        );
        Event debit = new Event(
                "EVT-002",
                "ACC-001",
                "DEBIT",
                new BigDecimal("1000"),
                "INR",
                Instant.parse("2026-09-23T11:00:00Z"),
                "source"
        );
        Event credit2 = new Event(
                "EVT-003",
                "ACC-001",
                "CREDIT",
                new BigDecimal("2000"),
                "INR",
                Instant.parse("2026-09-23T12:00:00Z"),
                "source"
        );
        when(eventRepo.findByAccountIdOrderByEventTimestampAsc(
                "ACC-001")).thenReturn(
                List.of(credit1, debit, credit2));
        BigDecimal balance = eventService.fetchBalanceForAccountId("ACC-001");
        assertEquals(new BigDecimal("6000"), balance);
        verify(eventRepo).findByAccountIdOrderByEventTimestampAsc("ACC-001");
    }
}