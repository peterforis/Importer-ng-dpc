package hu.dpc.phee.importerng;

import com.bazaarvoice.jolt.Chainr;
import com.bazaarvoice.jolt.JsonUtils;
import hu.dpc.phee.importerng.db.model.Event;
import hu.dpc.phee.importerng.db.repository.EventRepository;
import hu.dpc.phee.importerng.db.repository.ProcessDefinitionRepository;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TransactionParser {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ProcessDefinitionRepository processDefinitionRepository;

    private List<Chainr> chainrs;

    private List<String> specs;

    @PostConstruct
    public void setup() {
        loadSpecs();
        loadParsers();
    }

    public void loadParsers(){
        for(String spec: specs){
            String eventParserPath = "/parsers/" + spec;
            List<Object> chainrSpecJSON = JsonUtils.classpathToList(eventParserPath);
            LOG.info("Loaded parser from {}", eventParserPath);
            chainrs.add(Chainr.fromSpec(chainrSpecJSON));
        }
    }

    public void loadSpecs() {
        try {
            File directoryPath = new File("src/main/resources/parsers");
            File[] filesList = directoryPath.listFiles();
            if (filesList.length == 0) {
                LOG.error("No spec files found under /resources/parsers/");
                return;
            }
            for (File file : filesList) {
                StringBuilder stringBuilder = new StringBuilder();
                BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()));
                String line;
                while ((line = br.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                specs.add(stringBuilder.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean parseTransaction(String transaction, Chainr chainr) {
        Long processDefinitionKey = new JSONObject(transaction).getLong("processdefinitionkey");
        // TODO create caching for performance
        if (processDefinitionRepository.findByProcessDefinitionKey(processDefinitionKey).isEmpty()) {
            LOG.error("ProcessDefinition not found with processDefinitionKey: {}, transaction was: {}", processDefinitionKey, transaction);
            return false;
        }
        Object transformedOutput = chainr.transform(JsonUtils.jsonToObject(transaction));
        if (transformedOutput == null) {
            LOG.warn("Parsers did not parse transaction with processDefinitionKey: {}, transaction was: {}", processDefinitionKey, transaction);
            return false;
        }
        saveEvent(JsonUtils.toJsonString(transformedOutput));
        return true;
    }

    private void saveEvent(String parsedJsonString) {
        JSONObject json = new JSONObject(parsedJsonString);

        Long key = json.getLong("key");

        Event event = new Event();
        event.setKey(key);
        event.setProcessDefinitionKey(json.getLong("processdefinitionkey"));
        event.setVersion(json.getInt("version"));
        event.setTimeStamp(json.getLong("timestamp"));
        event.setEventText(json.get("eventtext").toString());

        eventRepository.save(event);

        LOG.debug("Saved event with key: {}", key);
    }

//    public void saveProcessDefinition(String joltParsedJson) {
//        JSONObject jsonObject = new JSONObject(joltParsedJson);
//
//        ProcessDefinition processDefinition = new ProcessDefinition();
//
//        processDefinition.setId((Integer) jsonObject.get("id"));
//        processDefinition.setProcessDefinitionKey((Long) jsonObject.get("processdefinitionkey"));
//        processDefinition.setVersion((Integer) jsonObject.get("version"));
//        processDefinition.setBpmnProcessid(jsonObject.get("timestamp").toString());
//        processDefinition.setResourceName(jsonObject.get("eventtext").toString());
//
//        processDefinitionRepository.save(processDefinition);
//
//        LOG.info("Parsed as PROCESSDEFINITION");
//    }

    public String jsonPrettyPrint(String jsonString) {
        return new JSONObject(jsonString).toString();
    }

    public List<Chainr> getChainrs() {
        return chainrs;
    }

    public void setChainrs(List<Chainr> chainrs) {
        this.chainrs = chainrs;
    }
}
