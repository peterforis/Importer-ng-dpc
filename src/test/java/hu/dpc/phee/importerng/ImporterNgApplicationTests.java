package hu.dpc.phee.importerng;

import com.bazaarvoice.jolt.Chainr;
import com.bazaarvoice.jolt.JsonUtils;
import org.junit.jupiter.api.Test;

import java.util.List;

class ImporterNgApplicationTests {

    String transaction = "{\"partitionId\":1,\"value\":{\"type\":\"hello-world\",\"errorMessage\":\"\",\"errorCode\":\"\",\"variables\":{},\"worker\":\"\",\"deadline\":-1,\"bpmnProcessId\":\"HelloProcess\",\"processDefinitionKey\":2251799813693651,\"customHeaders\":{},\"retries\":3,\"elementId\":\"HelloTask\",\"elementInstanceKey\":2251799813702305,\"processDefinitionVersion\":4,\"processInstanceKey\":2251799813693742},\"key\":2251799813702306,\"sourceRecordPosition\":41628,\"position\":41632,\"valueType\":\"JOB\",\"timestamp\":1629383865490,\"recordType\":\"EVENT\",\"rejectionReason\":\"\",\"brokerVersion\":\"1.1.0\",\"intent\":\"CREATED\",\"rejectionType\":\"NULL_VAL\"}";

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
