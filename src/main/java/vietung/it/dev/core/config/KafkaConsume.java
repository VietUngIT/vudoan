package vietung.it.dev.core.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.vertx.core.http.GoAway;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vietung.it.dev.core.models.kafka.SendQuestion;
import vietung.it.dev.core.models.kafka.SeparatedFrom;
import vietung.it.dev.core.services.ForumQuestionService;
import vietung.it.dev.core.services.imp.ForumQuestionServiceImp;
import vietung.it.dev.core.utils.Utils;

import java.util.Collections;

import java.util.Properties;

public class KafkaConsume {
    private static Logger logger = LoggerFactory.getLogger(KafkaConsume.class.getName());

    private final static String TOPIC = "question-data-visualization";
    private final static String BOOTSTRAP_SERVERS = "localhost:9092";

    private static Gson gson = null;
    private static Consumer<Long, String> consumer;

    private static Consumer<Long, String> createConsumer() {
        //gson
        gson = new Gson();
        //props
        final Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "KafkaExampleConsumer");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        // Create the consumer using props.
        final Consumer<Long, String> consumer = new KafkaConsumer<Long, String>(props);
        // Subscribe to the topic.
        consumer.subscribe(Collections.singletonList(TOPIC));
        return consumer;
    }

    private static void runConsumer() throws InterruptedException {
        consumer = createConsumer();

        while (true) {
            final ConsumerRecords<Long, String> consumerRecords =
                    consumer.poll(1000);

            consumerRecords.forEach(record -> {
                try {
                    SeparatedFrom separatedFrom = gson.fromJson(record.value(),SeparatedFrom.class);
                    ForumQuestionService service = new ForumQuestionServiceImp();
                    String strTags = Utils.toJsonStringGson(separatedFrom.getCountFroms());

                    logger.info(String.format("Consumer Record separatedFrom:(%s, %s)\n", separatedFrom.getId(), strTags));

                    service.getTagsForumQuestion(separatedFrom.getId(), strTags);

                    logger.info(String.format("Consumer Record:(%d, %s, %d, %d)\n", record.key(), record.value(), record.partition(), record.offset()));
                }catch (Exception e){
                    e.printStackTrace();
                    logger.error(String.format("Error(%s)\n",e.getMessage()));
                }

            });

            consumer.commitAsync();
        }
    }

    public static void stop(){
        consumer.close();
        System.out.println("DONE");
    }

    public static void init(){
        try {
            runConsumer();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
