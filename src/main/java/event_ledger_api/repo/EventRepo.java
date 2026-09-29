package event_ledger_api.repo;

import event_ledger_api.dto.EventResponse;
import event_ledger_api.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepo extends JpaRepository<Event,String> {

    List<Event> findByAccountIdOrderByEventTimestampAsc(String accountId);

}
