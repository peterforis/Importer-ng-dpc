package hu.dpc.phee.importerng;

import com.bazaarvoice.jolt.Chainr;
import com.bazaarvoice.jolt.JsonUtils;
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
        System.out.println("Basic message logging: " + message + " from partition: " + partition);
        System.out.println("Joltparsed message: " + joltParse(message) + " from partition: " + partition);
    }

    public String joltParse(String message) {
        try {
            List<Object> chainrSpecJSON = JsonUtils.classpathToList("/spec.json");
            Chainr chainr = Chainr.fromSpec(chainrSpecJSON);
            Object transformedOutput = chainr.transform(JsonUtils.jsonToObject(message));
            return JsonUtils.toJsonString(transformedOutput);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
