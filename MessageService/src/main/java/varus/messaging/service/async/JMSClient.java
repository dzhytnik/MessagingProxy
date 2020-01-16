package varus.messaging.service.async;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

@Component
public class JMSClient {
    public static final String BOOTSTRAP_SERVERS = "bootstrap.servers";
    public static final String ACKS = "acks";
    public static final String ALL = "all";
    public static final String KEY_SERIALIZER = "key.serializer";
    public static final String ORG_APACHE_KAFKA_COMMON_SERIALIZATION_STRING_SERIALIZER = "org.apache.kafka.common.serialization.StringSerializer";
    public static final String VALUE_SERIALIZER = "value.serializer";
    public static final String ORG_APACHE_KAFKA_COMMON_SERIALIZATION_STRING_SERIALIZER1 = "org.apache.kafka.common.serialization.StringSerializer";
    public static final String VARUS_MESSAGING_TOPIC = "varus-messaging-topic";
    public static final String VARUS_MESSAGING_SERVICE_KAFKA_SERVER = "localhost:9092";


    Producer<String, String> producer;

    private JMSClient() {
        Properties props = new Properties();
        props.put(BOOTSTRAP_SERVERS, VARUS_MESSAGING_SERVICE_KAFKA_SERVER);
        props.put(ACKS, ALL);
        props.put(KEY_SERIALIZER, ORG_APACHE_KAFKA_COMMON_SERIALIZATION_STRING_SERIALIZER);
        props.put(VALUE_SERIALIZER, ORG_APACHE_KAFKA_COMMON_SERIALIZATION_STRING_SERIALIZER1);

        producer = new KafkaProducer<>(props);
    }

    public void sendJMSMessage(String message) {
        producer.send(new ProducerRecord<String, String>(VARUS_MESSAGING_TOPIC, message));
    }
}
