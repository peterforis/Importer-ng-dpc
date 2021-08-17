package hu.dpc.phee.importerng;

import com.bazaarvoice.jolt.Chainr;
import com.bazaarvoice.jolt.JsonUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

class ImporterNgApplicationTests {

	String transaction = "{\"partitionId\":1,\"value\":{\"type\":\"hello-world\",\"errorMessage\":\"\",\"errorCode\":\"\",\"variables\":{\"demoKey\":\"785972\",\"name\":\"123\",\"message\":\"I am a message 123\"},\"worker\":\"default\",\"deadline\":1629213895816,\"bpmnProcessId\":\"HelloProcess\",\"processDefinitionKey\":2251799813693651,\"customHeaders\":{},\"retries\":3,\"elementId\":\"HelloTask\",\"elementInstanceKey\":2251799813696501,\"processDefinitionVersion\":4,\"processInstanceKey\":2251799813693742},\"key\":2251799813696502,\"sourceRecordPosition\":25148,\"valueType\":\"JOB\",\"timestamp\":1629213595835,\"recordType\":\"EVENT\",\"rejectionReason\":\"\",\"brokerVersion\":\"1.1.0\",\"intent\":\"COMPLETED\",\"rejectionType\":\"NULL_VAL\",\"position\":25150}";

	@Test
	public void parse(){
		System.out.println("Setup started");
		String eventParserPath = "/parsers/spec_filter_2.json";
		List<Object> chainrSpecJSON = JsonUtils.classpathToList(eventParserPath);
		Chainr chainr = Chainr.fromSpec(chainrSpecJSON);
		System.out.println("Setup ended");

		Object transformedOutput = chainr.transform(JsonUtils.jsonToObject(transaction));
		if (transformedOutput == null) {
			System.out.println("Did not parse");
		}else{
			System.out.println((JsonUtils.toJsonString(transformedOutput)));
		}

	}

}
