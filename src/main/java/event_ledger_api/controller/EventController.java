package event_ledger_api.controller;

import event_ledger_api.dto.EventDto;
import event_ledger_api.dto.EventResponse;
import event_ledger_api.dto.EventSaveResult;
import event_ledger_api.dto.PagedResponse;
import event_ledger_api.service.EventService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
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
    public ResponseEntity<PagedResponse<EventResponse>> fetchEventsWithAccountId(
            @RequestParam("account") String account,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        PageRequest pageRequest = PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 100));
        return ResponseEntity.ok(
                PagedResponse.from(eventService.fetchEventsByAccount(account, pageRequest)));
    }

}
