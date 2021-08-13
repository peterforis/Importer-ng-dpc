package hu.dpc.phee.importerng.db.service;

import hu.dpc.phee.importerng.db.model.Event;

import java.util.Optional;

public interface EventService {

    Iterable<Event> findAll();

    Optional<Event> findById(long id);

    void save(Event event);

    void deleteById(long id);

    long count();
}
