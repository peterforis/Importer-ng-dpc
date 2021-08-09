package hu.dpc.phee.importerng.db.service;

import hu.dpc.phee.importerng.db.model.Message;

public interface MessageService {

    Iterable<Message> findAll();
}
