package hu.dpc.phee.importerng.db.service;

import hu.dpc.phee.importerng.db.model.Message;
import hu.dpc.phee.importerng.db.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public Iterable<Message> findAll() {
        return messageRepository.findAll();
    }
}
