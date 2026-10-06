package event_ledger_api.controller;

import event_ledger_api.dto.EventDto;
import event_ledger_api.dto.EventResponse;
import event_ledger_api.dto.EventSaveResult;
import event_ledger_api.service.EventService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/events")

public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<EventResponse> saveEvent(@Valid @RequestBody EventDto dto) {

        EventSaveResult resultEvent = eventService.saveEvent(dto);

        if(resultEvent.isDuplicate())
        {
            return new ResponseEntity<>(resultEvent.getEvent(),HttpStatus.OK);
        }
        return new ResponseEntity<>(resultEvent.getEvent(), HttpStatus.CREATED);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponse> fetchEventById(@Valid @PathVariable String eventId) {

        return new ResponseEntity<>(eventService.fetchEventById(eventId),HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<EventResponse>> fetchEventsWithAccountId(
            @RequestParam String account, @PageableDefault(size = 10) Pageable pageable) {

        return ResponseEntity.ok(eventService.fetchEventsByAccount(account, pageable));
    }

}
