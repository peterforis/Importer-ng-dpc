package hu.dpc.phee.importerng;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    @Value("con.url")
    private String url;

    private Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TransactionParser transactionParser;

    @KafkaListener(topics = "zeebe-export", groupId = "importer-ng")
    public void listenToPartition(@Payload String transaction, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
//        TODO: chainr need to be changed later
        boolean parsed = transactionParser.parseTransaction(transaction, transactionParser.getChainrs().get(0));
        if (!parsed) {
            LOG.error("Could not parse: {}", transaction);
        }
    }
}
