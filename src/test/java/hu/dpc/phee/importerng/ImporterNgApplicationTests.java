package hu.dpc.phee.importerng;

import com.bazaarvoice.jolt.Chainr;
import com.bazaarvoice.jolt.JsonUtils;
import org.junit.jupiter.api.Test;

import java.util.List;

class ImporterNgApplicationTests {

    String transaction = "\"partitionId\":1,\"value\":{\"name\":\"hellonumber\",\"value\":\"2\",\"processDefinitionKey\":2251799813691970,\"processInstanceKey\":2251799813692700,\"scopeKey\":2251799813692700},\"key\":2251799813692718,\"sourceRecordPosition\":19743,\"position\":19746,\"valueType\":\"VARIABLE\",\"timestamp\":1629902023868,\"recordType\":\"EVENT\",\"intent\":\"CREATED\",\"rejectionType\":\"NULL_VAL\",\"rejectionReason\":\"\",\"brokerVersion\":\"1.1.0\"}";
    @Test
    public void parse() {
        System.out.println("Setup started");
        String eventParserPath = "/parsers/spec_filter_2.json";
        List<Object> chainrSpecJSON = JsonUtils.classpathToList(eventParserPath);
        Chainr chainr = Chainr.fromSpec(chainrSpecJSON);
        System.out.println("Setup ended");

        Object transformedOutput = chainr.transform(JsonUtils.jsonToObject(transaction));
        if (transformedOutput == null) {
            System.out.println("Did not parse");
        } else {
            System.out.println((JsonUtils.toJsonString(transformedOutput)));
        }

    }

}
