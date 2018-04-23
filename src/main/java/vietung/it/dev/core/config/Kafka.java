package vietung.it.dev.core.config;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

public class Kafka {

    private static KafkaProducer<String, String> producer;
    private static String topic = "bike-data";
    private static String brokers = "localhost:9092";

    public static void init() throws IOException {

        int events = 0;

        int intervalEvent = 30;

        int rndStart = 0;

        int rndEnd = 500;

        //Create some properties

        Properties props = new Properties();
        props.put("bootstrap.servers", brokers);
        props.put("client.id", UUID.randomUUID().toString());
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        producer = new KafkaProducer<String, String>(props);

    }


    public static void send(String id , String idfieid ,String content) {
        String key = UUID.randomUUID().toString().split("-")[0];
//        Gson gson = new Gson();
//        String value = gson.toJson(separatedFrom);
        String value = id+"/#/"+idfieid + "/#/" + content;
        ProducerRecord<String, String> data = new ProducerRecord<String, String>(topic, key, value);

        System.out.println("--- topic: " + topic + " ---");
        System.out.println("key: " + data.key());
        System.out.println("value: " + data.value() + "\n");
        producer.send(data);
    }

    public static void send2(String id , String idfieid ,String content) {
        int events = 0;

        int intervalEvent = 30;

        int rndStart = 0;

        int rndEnd = 500;

        //Create some properties

        Properties props = new Properties();
        props.put("bootstrap.servers", brokers);
        props.put("client.id", UUID.randomUUID().toString());
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        KafkaProducer<String, String> producer2 = new KafkaProducer<String, String>(props);

        String key = UUID.randomUUID().toString().split("-")[0];
//        Gson gson = new Gson();
//        String value = gson.toJson(separatedFrom);
        String value = id+"/#/"+idfieid + "/#/" + content;
        ProducerRecord<String, String> data = new ProducerRecord<String, String>(topic, key, value);

        System.out.println("--- topic: " + topic + " ---");
        System.out.println("key: " + data.key());
        System.out.println("value: " + data.value() + "\n");
        producer2.send(data);
    }

    public static void out(String id , String idfieid ,String content){
        //Get the Kafka broker node
        String brokers = "localhost:9092";

        //Get the exists topic named welcome-message
        String topic = "bike-data";

        int events = 0;

        int intervalEvent = 30; //in second

        int rndStart = 0;//in second

        int rndEnd = 500; //in second


        //Create some properties
        Properties props = new Properties();
        props.put("bootstrap.servers", brokers);
        props.put("client.id", UUID.randomUUID().toString());
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer producer = new KafkaProducer<String, String>(props);

        //The while loop will generate the data and send to Kafka

//            while (true) {
                String key = UUID.randomUUID().toString().split("-")[0];
                String value = id+"/#/"+idfieid + "/#/" + content;
                ProducerRecord<String, String> data = new ProducerRecord<String, String>(topic, key, value);

                producer.send(data);
//                try {
//                    Thread.sleep( 1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//
//            }

    }

}
