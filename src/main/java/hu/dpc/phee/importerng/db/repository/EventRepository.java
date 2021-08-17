package hu.dpc.phee.importerng.db.repository;

import hu.dpc.phee.importerng.db.model.Event;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface EventRepository extends CrudRepository<Event, Long> {
    @Override
    <S extends Event> S save(S s);

    @Override
    Optional<Event> findById(Long aLong);

    @Override
    Iterable<Event> findAll();

    @Override
    void deleteById(Long aLong);

    Optional<Event> findByProcessDefinitionKey(Long processDefinitionKey);

}
