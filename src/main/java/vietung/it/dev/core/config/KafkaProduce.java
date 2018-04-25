package vietung.it.dev.core.config;

import com.google.gson.Gson;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vietung.it.dev.apis.launcher.APILauncher;
import vietung.it.dev.core.models.kafka.CountFrom;
import vietung.it.dev.core.models.kafka.SendQuestion;
import vietung.it.dev.core.models.kafka.SeparatedFrom;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class KafkaProduce {
    private static Logger logger = LoggerFactory.getLogger(KafkaProduce.class.getName());

    private final static String TOPIC = "question-data";
    private final static String BOOTSTRAP_SERVERS = "localhost:9092";
    private static Producer<Long, String> producer;

    private static Producer<Long, String> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "KafkaExampleProducer");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return new KafkaProducer<>(props);
    }

    public static void runProducer(final String id, final String content) throws Exception {
        long time = System.currentTimeMillis();
        try {
            SendQuestion sendQuestion = new SendQuestion(id, content);
            Gson gson = new Gson();
            String value = gson.toJson(sendQuestion);
            final ProducerRecord<Long, String> record = new ProducerRecord<>(TOPIC, time, value);
            RecordMetadata metadata = producer.send(record).get();
            long elapsedTime = System.currentTimeMillis() - time;
            logger.info(String.format("sent record(key=%d value=%s) meta(partition=%d, offset=%d) time=%d\n", record.key(), record.value(), metadata.partition(), metadata.offset(), elapsedTime));

        } catch (Exception e) {
            logger.error(String.format("Error(%s)\n", e.getMessage()));
        }
    }

    public static void stop() {
        producer.flush();
        producer.close();
    }

    public static void init() {
        producer = createProducer();
    }

}
