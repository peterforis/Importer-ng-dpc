package hu.dpc.phee.importerng;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    private Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TransactionParser transactionParser;

    @KafkaListener(topics = "zeebe-export", groupId = "importer-ng")
    public void listenToPartition(@Payload String transaction, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
        boolean parsed = transactionParser.parseTransaction(transaction);
        if (!parsed) {
            LOG.warn("Could not parse: {}", transaction);
        }
    }
}
