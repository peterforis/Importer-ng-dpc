package hu.dpc.phee.importerng.db.service;

import hu.dpc.phee.importerng.db.model.ProcessDefinition;
import hu.dpc.phee.importerng.db.repository.ProcessDefinitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProcessDefinitionServiceImpl implements ProcessDefinitionService {

    @Autowired
    private ProcessDefinitionRepository processDefinitionRepository;

    @Override
    public Iterable<ProcessDefinition> findAll() {
        return processDefinitionRepository.findAll();
    }

    @Override
    public Optional<ProcessDefinition> findById(long id) {
        return processDefinitionRepository.findById(id);
    }

    @Override
    public void save(ProcessDefinition processDefinition) {
        processDefinitionRepository.save(processDefinition);
    }

    @Override
    public void deleteById(long id) {
        processDefinitionRepository.deleteById(id);
    }

    @Override
    public long count() {
        return processDefinitionRepository.count();
    }


    public Optional<ProcessDefinition> findByProcessDefintionKey(Long processDefinitionKey){
        return processDefinitionRepository.findByProcessDefinitionKey(processDefinitionKey);
    }
}
