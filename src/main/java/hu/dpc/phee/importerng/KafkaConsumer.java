package hu.dpc.phee.importerng;

import com.bazaarvoice.jolt.Chainr;
import com.bazaarvoice.jolt.JsonUtils;
import hu.dpc.phee.importerng.db.model.Message;
import hu.dpc.phee.importerng.db.repository.MessageRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KafkaConsumer {

    @Autowired
    private MessageRepository messageRepository;

    @KafkaListener(topics = "zeebe-export", groupId = "importer-ng")
    public void listenToPartition(@Payload String incomingMessage, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
        String joltparsedJson = joltParse(incomingMessage, "/spec_filter_COMPLETED.json");
        if (joltparsedJson == null) {
            System.out.println("New basic message: " + incomingMessage);
        } else {
            JSONObject jsonObject = new JSONObject(joltparsedJson);

            Message message = new Message();
            message.setId((Long) jsonObject.get("id"));
            message.setValueType(jsonObject.get("valuetype").toString());
            message.setTimeStamp((Long) jsonObject.get("timestamp"));
            message.setRecordType(jsonObject.get("recordtype").toString());
            message.setIntent(jsonObject.get("intent").toString());
            messageRepository.save(message);

            System.out.println("New joltparsed message: " + joltparsedJson);
        }
    }

    public String joltParse(String message, String parserPath) {
        // TODO: runtime -> uncatched
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
