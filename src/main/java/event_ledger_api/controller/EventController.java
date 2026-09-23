package event_ledger_api.controller;

import event_ledger_api.serviceimpl.EventServiceImpl;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventController {

    private EventServiceImpl eventServiceImpl;

    public EventController(EventServiceImpl eventServiceImpl)
    {
        this.eventServiceImpl = eventServiceImpl;
    }

}
