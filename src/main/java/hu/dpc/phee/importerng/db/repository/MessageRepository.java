package hu.dpc.phee.importerng.db.repository;

import hu.dpc.phee.importerng.db.model.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {

}