package event_ledger_api.repo;

import event_ledger_api.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepo extends JpaRepository<Event,String> {

}
