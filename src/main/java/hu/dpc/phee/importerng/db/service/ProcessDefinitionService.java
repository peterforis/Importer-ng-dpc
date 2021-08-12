package hu.dpc.phee.importerng.db.service;

import hu.dpc.phee.importerng.db.model.ProcessDefinition;

import java.util.Optional;

public interface ProcessDefinitionService {

    Iterable<ProcessDefinition> findAll();

    Optional<ProcessDefinition> findById(long id);

    void save(ProcessDefinition processDefinition);

    void deleteById(long id);

    long count();
}
