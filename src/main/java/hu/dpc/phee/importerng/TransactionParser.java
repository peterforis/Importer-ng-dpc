package hu.dpc.phee.importerng;

import com.bazaarvoice.jolt.Chainr;
import com.bazaarvoice.jolt.JsonUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class TransactionParser {

    private Logger LOG = LoggerFactory.getLogger(this.getClass());

    private Chainr chainr;

    private Connection conn;

//    @Autowired
//    EventRepository eventRepository;

    public static final String INSERT_SQL_QUERY = "INSERT INTO EVENT VALUES(?,?,?,?,?)";
    public static final String SELECT_SQL_QUERY_BY_ID_FROM_EVENT = "SELECT * FROM EVENT WHERE ID=?";
    public static final String SELECT_SQL_QUERY_BY_PROCESS_DEFINITION_KEY_FROM_PROCESSDEFINITION = "SELECT * FROM PROCESSDEFINITION WHERE PROCESSDEFINITIONKEY=?";

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
        System.out.println(jsonPrettyPrint(transaction));
        Long processDefinitionKey = new JSONObject(transaction).getLong("sourceRecordPosition");
        // TODO create caching for performance

        try {
            PreparedStatement ps = conn.prepareStatement(SELECT_SQL_QUERY_BY_PROCESS_DEFINITION_KEY_FROM_PROCESSDEFINITION);
            ps.setLong(1, processDefinitionKey);
            boolean processDefinitionExists = ps.execute();
            conn.commit();
            if (!processDefinitionExists) {
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

        try {
            PreparedStatement ps = conn.prepareStatement(INSERT_SQL_QUERY);
            ps.setLong(1, key);
            ps.setLong(2, json.getLong("processdefinitionkey"));
            ps.setInt(3, json.getInt("version"));
            ps.setLong(4, json.getLong("timestamp"));
            ps.setString(5, json.getString("eventtext"));
            ps.execute();
            conn.commit();
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
