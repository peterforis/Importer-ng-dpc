package hu.dpc.phee.importerng.db.repository;

import hu.dpc.phee.importerng.db.model.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends CrudRepository<Event, Long> {

    @Override
    Iterable<Event> findAll();

    @Override
    Iterable<Event> findAllById(Iterable<Long> iterable);

    @Override
    <S extends Event> S save(S s);

    @Override
    void deleteById(Long aLong);

    @Override
    long count();

}