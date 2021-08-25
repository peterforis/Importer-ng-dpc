package hu.dpc.phee.importerng.db.repository;

import hu.dpc.phee.importerng.db.model.Event;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

@Repository
public interface EventRepository extends CrudRepository<Event, Long> {

    Optional<Event> findByProcessDefinitionKey(Long processDefinitionKey);

}
