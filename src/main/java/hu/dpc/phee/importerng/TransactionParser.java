package hu.dpc.phee.importerng;

import com.bazaarvoice.jolt.Chainr;
import com.bazaarvoice.jolt.JsonUtils;
import hu.dpc.phee.importerng.db.model.Event;
import hu.dpc.phee.importerng.db.model.ProcessDefinition;
import hu.dpc.phee.importerng.db.repository.EventRepository;
import hu.dpc.phee.importerng.db.repository.ProcessDefinitionRepository;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

public class TransactionParser {

    private Logger LOG = LoggerFactory.getLogger(this.getClass());

    private Chainr chainr;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    ProcessDefinitionRepository processDefinitionRepository;

    @PostConstruct
    public void setup() {
        // TODO add parser
        String eventParserPath = "/parsers/spec_filter_1.json";
        List<Object> chainrSpecJSON = JsonUtils.classpathToList(eventParserPath);
        LOG.info("Loaded parser from {}", eventParserPath);
        chainr = Chainr.fromSpec(chainrSpecJSON);
    }

    public boolean parseTransaction(String transaction) {

        LOG.info("transaction" + transaction);

        Object transformedOutput = chainr.transform(JsonUtils.jsonToObject(transaction));
        if (transformedOutput == null) {
            LOG.warn("Parsers did not parse transaction, transaction was: {}", transaction);
            return false;
        }

        // TODO create caching for performance
        JSONObject json = new JSONObject(transformedOutput);

        LOG.info("transaction2" + json);

        Long processDefinitionKey = json.getLong("processDefinitionKey");
        Optional<ProcessDefinition> processDefinition = processDefinitionRepository.findByProcessDefinitionKey(processDefinitionKey);
        if (processDefinition.isEmpty()) {
            LOG.warn("Could not find a processDefinition with processDefinitionKey {}, transaction was {}", processDefinitionKey, transaction);
            return false;
        }

        saveEvent(json);
        return true;
    }

    private void saveEvent(JSONObject json) {

        Long key = json.getLong("key");

        Event event = new Event();
        event.setKey(key);
        event.setProcessDefinitionKey(json.getLong("processdefinitionkey"));
        event.setVersion(json.getInt("version"));
        event.setTimeStamp(json.getLong("timestamp"));
        event.setEventText(json.getString("eventtext"));

        eventRepository.save(event);

        LOG.debug("Saved event with key: {}", key);
    }
}
