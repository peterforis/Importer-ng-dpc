package hu.dpc.phee.importerng;

import com.bazaarvoice.jolt.Chainr;
import com.bazaarvoice.jolt.JsonUtils;
import org.junit.jupiter.api.Test;

import java.util.List;

class ImporterNgApplicationTests {

    String transaction = "{\n" +
            "  \"partitionId\": 1,\n" +
            "  \"value\": {\n" +
            "    \"type\": \"hello-world\",\n" +
            "    \"errorMessage\": \"\",\n" +
            "    \"errorCode\": \"\",\n" +
            "    \"variables\": {\n" +
            "      \"demoKey\": \"7aeb33f2-a75b-4db4-943a-8e06c78609dc\",\n" +
            "      \"name\": \"123\",\n" +
            "      \"message\": \"I am a message 123\"\n" +
            "    },\n" +
            "    \"worker\": \"default\",\n" +
            "    \"deadline\": 1629889469057,\n" +
            "    \"bpmnProcessId\": \"HelloProcess\",\n" +
            "    \"processDefinitionKey\": 2251799813685398,\n" +
            "    \"customHeaders\": {},\n" +
            "    \"retries\": 3,\n" +
            "    \"elementId\": \"HelloTask\",\n" +
            "    \"elementInstanceKey\": 2251799813685502,\n" +
            "    \"processDefinitionVersion\": 1,\n" +
            "    \"processInstanceKey\": 2251799813685400\n" +
            "  },\n" +
            "  \"key\": 2251799813685503,\n" +
            "  \"sourceRecordPosition\": 567,\n" +
            "  \"position\": 569,\n" +
            "  \"recordType\": \"EVENT\",\n" +
            "  \"valueType\": \"JOB\",\n" +
            "  \"timestamp\": 1629889169075,\n" +
            "  \"intent\": \"CREATED\",\n" +
            "  \"rejectionType\": \"NULL_VAL\",\n" +
            "  \"rejectionReason\": \"\",\n" +
            "  \"brokerVersion\": \"1.1.0\"\n" +
            "}";



    @Test
    public void parse() {
        System.out.println("Setup started");
        String eventParserPath = "/parsers/spec_filter_job_created.json";
        List<Object> chainrSpecJSON = JsonUtils.classpathToList(eventParserPath);
        Chainr chainr = Chainr.fromSpec(chainrSpecJSON);
        System.out.println("Setup ended");

        Object transformedOutput = chainr.transform(JsonUtils.jsonToObject(transaction));
            System.out.println((JsonUtils.toJsonString(transformedOutput)));

    }

}
