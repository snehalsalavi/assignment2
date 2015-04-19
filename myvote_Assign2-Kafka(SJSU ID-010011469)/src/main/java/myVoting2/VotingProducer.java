package myVoting2;

import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class VotingProducer {
	 private static Producer<Integer, String> producer;
	 private static Properties properties = new Properties();
	 public static String topic="cmpe273-topic";

	    public static Producer<Integer, String> getResultProducer() {
	        properties.put("metadata.broker.list", "54.149.84.25:9092");
	        properties.put("serializer.class", "kafka.serializer.StringEncoder");
	        properties.put("request.required.acks", "1");
	        return producer = new Producer<>(new ProducerConfig(properties));
	    }

}