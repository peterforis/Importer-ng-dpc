package hu.dpc.phee.importerng.db.service;

import hu.dpc.phee.importerng.db.model.Event;
import hu.dpc.phee.importerng.db.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Override
    public Iterable<Event> findAll() {
        return eventRepository.findAll();
    }

    @Override
    public Optional<Event> findById(long id) {
        return eventRepository.findById(id);
    }

    @Override
    public void save(Event event) {
        eventRepository.save(event);
    }

    @Override
    public void deleteById(long id) {
        eventRepository.deleteById(id);
    }

    @Override
    public long count() {
        return eventRepository.count();
    }
}
