package hu.dpc.phee.importerng;

import com.bazaarvoice.jolt.Chainr;
import com.bazaarvoice.jolt.JsonUtils;
import hu.dpc.phee.importerng.db.model.Message;
import hu.dpc.phee.importerng.db.model.ProcessDefinition;
import hu.dpc.phee.importerng.db.repository.MessageRepository;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public class JoltParser {

    private Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MessageRepository messageRepository;

    @Value("${parserPaths}")
    private List<String> parsersPaths;

    public boolean joltParse(String message) {
        for (String parserPath : parsersPaths) {
            List<Object> chainrSpecJSON = JsonUtils.classpathToList("/" + parserPath);
            Chainr chainr = Chainr.fromSpec(chainrSpecJSON);
            Object transformedOutput = chainr.transform(JsonUtils.jsonToObject(message));
            if (transformedOutput != null) {
                save(JsonUtils.toJsonString(transformedOutput));
                return true;
            }
        }
        return false;
    }

    public void save(String joltParsedJson) {
        JSONObject jsonObject = new JSONObject(joltParsedJson);
        switch (jsonObject.get("filterId").toString()) {
            case "1":
                Message message = new Message();
                message.setKey((Long) jsonObject.get("key"));
                message.setValueType(jsonObject.get("valueType").toString());
                message.setTimeStamp((Long) jsonObject.get("timestamp"));
                message.setRecordType(jsonObject.get("recordType").toString());
                messageRepository.save(message);
                LOG.info("Parsed as MESSAGE with parser 1");
                break;
            case "2":
                //TODO: implement
                ProcessDefinition processDefinition = new ProcessDefinition();
                LOG.info("Parsed as PROCESSDEFINITION with parser 2");
                break;
            default:
                LOG.info("JoltParser.save error");
        }
    }

    public String jsonPrettyPrint(String jsonString) {
        return new JSONObject(jsonString).toString();
    }
}
