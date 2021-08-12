package hu.dpc.phee.importerng.db.repository;

import hu.dpc.phee.importerng.db.model.ProcessDefinition;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProcessDefinitionRepository extends CrudRepository<ProcessDefinition, Long> {
    @Override
    <S extends ProcessDefinition> S save(S s);

    @Override
    Optional<ProcessDefinition> findById(Long aLong);

    @Override
    Iterable<ProcessDefinition> findAll();

    @Override
    long count();

    @Override
    void deleteById(Long aLong);
}