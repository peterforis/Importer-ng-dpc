package hu.dpc.phee.importerng.db.repository;

import hu.dpc.phee.importerng.db.model.ProcessDefinition;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProcessDefinitionRepository extends CrudRepository<ProcessDefinition, Long> {

    Optional<ProcessDefinition> findByProcessDefinitionKey(Long processDefinitionKey);

}
