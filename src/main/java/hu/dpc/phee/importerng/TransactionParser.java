package hu.dpc.phee.importerng;

import com.bazaarvoice.jolt.Chainr;
import com.bazaarvoice.jolt.JsonUtils;
import hu.dpc.phee.importerng.db.model.Event;
import hu.dpc.phee.importerng.db.model.ProcessDefinition;
import hu.dpc.phee.importerng.db.repository.EventRepository;
import hu.dpc.phee.importerng.db.repository.ProcessDefinitionRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.*;

@Component
public class TransactionParser {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    EventRepository eventRepository;

    @Autowired
    ProcessDefinitionRepository processDefinitionRepository;

    private Map<String, Chainr> chainrs = new HashMap<>();

    @PostConstruct
    public void setup() {
        ArrayList<String> specs = new ArrayList<>();
        File directoryPath = new File("src/main/resources/parsers");
        File[] filesList = directoryPath.listFiles();
        if (filesList.length == 0) {
            LOG.error("No spec files found under /resources/parsers/");
            return;
        }
        for (File file : filesList) {
            specs.add(file.getName());
        }

        for (String spec : specs) {
            String eventParserPath = "/parsers/" + spec;
            List<Object> chainrSpecJSON = JsonUtils.classpathToList(eventParserPath);
            LOG.info("Loaded parser from {}", eventParserPath);
            chainrs.put(spec, Chainr.fromSpec(chainrSpecJSON));
        }
    }

    public boolean parseTransaction(String transaction) {
        for (String key : chainrs.keySet()) {
            Object transformedOutput = chainrs.get(key).transform(JsonUtils.jsonToObject(transaction));
            if (transformedOutput == null) {
                continue;
            }

            // TODO create caching for performance
            JSONObject json = new JSONObject(JsonUtils.toJsonString(transformedOutput));

            if (isDeploymentEvent(key)) {
                saveProcessDefinition(json);
            } else {
                saveEvent(json);
            }
            return true;
        }
        return false;
    }

    private boolean isDeploymentEvent(String key) {
        return "event_deployment_created_spec.json".equals(key);
    }

    private void saveEvent(JSONObject json) {

        if (json.has("processDefinitionKey")) {
            Long processDefinitionKey = json.getLong("processDefinitionKey");
            Optional<ProcessDefinition> processDefinition = processDefinitionRepository.findByProcessDefinitionKey(processDefinitionKey);
            if (processDefinition.isEmpty()) {
                LOG.warn("Could not find a processDefinition with processDefinitionKey {}", processDefinitionKey);
                return;
            }
        }

        try {
            Long key = json.getLong("key");

            Event event = new Event();
            event.setKey(key);
            event.setProcessDefinitionKey(json.getJSONObject("eventText").getLong("processDefinitionKey"));
            event.setVersion(1);
            event.setTimeStamp(json.getLong("timestamp"));
            event.setEventText(json.getJSONObject("eventText").toString());
            eventRepository.save(event);
            LOG.info("Saved event with key: {}", key);
        } catch (JSONException e) {
            LOG.error(e.toString(), e);
        }

    }

    private void saveProcessDefinition(JSONObject json) {
        try {
            int id = json.getInt("id");

            ProcessDefinition processDefinition = new ProcessDefinition();
            processDefinition.setId(id);
            processDefinition.setProcessDefinitionKey(json.getLong("processDefinitionKey"));
            processDefinition.setVersion(json.getInt("version"));
            processDefinition.setBpmnProcessId(json.getString("bpmnProcessId"));
            processDefinition.setResourceName(json.getString("resourceName"));

            processDefinitionRepository.save(processDefinition);
            LOG.debug("Saved processDefinition with id: {}", id);
        } catch (JSONException e) {
            LOG.info("Did not save processDefinition, missing field");
        }
    }
}
