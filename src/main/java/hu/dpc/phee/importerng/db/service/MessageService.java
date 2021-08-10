package hu.dpc.phee.importerng.db.service;

import hu.dpc.phee.importerng.db.model.Message;

import java.util.Optional;

public interface MessageService {

    Iterable<Message> findAll();

    Optional<Message> findById(long id);

    void save(Message message);

    void deleteById(long id);

    long count();
}
