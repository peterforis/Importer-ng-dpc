package hu.dpc.phee.importerng;

import com.bazaarvoice.jolt.Chainr;
import com.bazaarvoice.jolt.JsonUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.util.List;

public class TransactionParser {

    private Logger LOG = LoggerFactory.getLogger(this.getClass());

    private Chainr chainr;

    private Connection conn;

    @PostConstruct
    public void setup() {
        // TODO add parser
        String eventParserPath = "/parsers/spec_filter_1.json";
        List<Object> chainrSpecJSON = JsonUtils.classpathToList(eventParserPath);
        LOG.info("Loaded {} parsers from {}", chainrSpecJSON.size(), eventParserPath);
        chainr = Chainr.fromSpec(chainrSpecJSON);

        String url = "jdbc:postgresql://localhost:5432/testdb";
        String username = "postgres";
        String password = "postgres";
        try {
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
        }
    }

    public boolean parseTransaction(String transaction) {
        Long processDefinitionKey = new JSONObject(transaction).getLong("processdefinitionkey");
        // TODO create caching for performance
        String getByprocessDefinitionKey = "SELECT COUNT(*) AS total FROM PROCESSDEFINITION WHERE PROCESSDEFINITIONKEY = " + processDefinitionKey;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(getByprocessDefinitionKey);
            if (rs.getInt("total") == 0) {
                LOG.error("ProcessDefinition not found with processDefinitionKey: {}, transaction was: {}", processDefinitionKey, transaction);
                return false;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
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

        String createEvent = "INSERT INTO EVENT VALUES + ("
                + key + ", "
                + json.getLong("processdefinitionkey") + ", "
                + json.getInt("version") + ", "
                + json.getLong("timestamp") + ", "
                + json.get("eventtext") + ")";
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(createEvent);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
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
}
