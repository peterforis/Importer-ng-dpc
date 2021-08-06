package hu.dpc.phee.importerng;

import com.bazaarvoice.jolt.Chainr;
import com.bazaarvoice.jolt.JsonUtils;
import org.json.JSONObject;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KafkaConsumer {

    @KafkaListener(topics = "zeebe-export", groupId = "importer-ng")
    public void listenToPartition(@Payload String message, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
        String joltparsedJson = joltParse(message, "/spec_filter_COMPLETED.json");
        if (joltparsedJson == null) {
            System.out.println("New basic message: " + message);
        } else {
            System.out.println("New joltparsed message: " + joltparsedJson);
        }
    }

    public String joltParse(String message, String parserPath) {
        try {
            List<Object> chainrSpecJSON = JsonUtils.classpathToList(parserPath);
            Chainr chainr = Chainr.fromSpec(chainrSpecJSON);
            Object transformedOutput = chainr.transform(JsonUtils.jsonToObject(message));
            if (transformedOutput == null) {
                return null;
            }
            return JsonUtils.toJsonString(transformedOutput);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String jsonPrettyPrint(String jsonString) {
        return new JSONObject(jsonString).toString();
    }
}
