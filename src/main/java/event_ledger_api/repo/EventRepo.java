package event_ledger_api.repo;


import event_ledger_api.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface EventRepo extends JpaRepository<Event,String> {

    List<Event> findByAccountIdOrderByEventTimestampAsc(String accountId);

    Page<Event> findByAccountIdOrderByEventTimestampAsc(String accountId, Pageable pageable);


}
