package hu.dpc.phee.importerng.db.repository;

import hu.dpc.phee.importerng.db.model.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {

    @Override
    Iterable<Message> findAll();

    @Override
    Iterable<Message> findAllById(Iterable<Long> iterable);

    @Override
    <S extends Message> S save(S s);

    @Override
    void deleteById(Long aLong);

    @Override
    long count();

}