package ru.leeeny.dispatcherservice.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import ru.leeeny.restaurant.AvroOrderPlacedEvent;
import ru.leeeny.restaurant.OrderDispatchStatus;
import ru.leeeny.restaurant.OrderDispatchedEvent;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class DispatcherService {

	private final KafkaTemplate<String, OrderDispatchedEvent> kafkaTemplate;

	@Value("${kafkaprops.order-dispatch-topic}")
	private String orderDispatchTopic;

	@Value("${kafkaprops.nack-sleep-duration}")
	private Duration nackSleepDuration;

	@KafkaListener(topics = {"${kafkaprops.order-placed-topic}"})
	public void consumeOrderPlacedEvent(AvroOrderPlacedEvent event,
	                                    @Header(KafkaHeaders.RECEIVED_KEY) String key,
	                                    @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
	                                    @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
	                                    Acknowledgment acknowledgment) {
		log.info("Received order placed event {}. Key: {}, Partition: {}, Topic: {}", event, key, partition, topic);
		var orderDispatchedEvent = processEvent(event);

		try {
			kafkaTemplate.send(orderDispatchTopic, key, orderDispatchedEvent).get();
			log.info("Sent order placed event {}", orderDispatchedEvent);

			acknowledgment.acknowledge();
		} catch (Exception _) {
			log.error("Failed to send order placed event {}", orderDispatchedEvent);
			acknowledgment.nack(nackSleepDuration);

			Thread.currentThread().interrupt();
		}

	}

	private OrderDispatchedEvent processEvent(AvroOrderPlacedEvent event) {
		log.info("Processing order placed event {}", event);

		OrderDispatchStatus status = event.getOrderId() % 2 == 0 ? OrderDispatchStatus.ACCEPTED : OrderDispatchStatus.REJECTED;

		return OrderDispatchedEvent.newBuilder()
				.setOrderId(event.getOrderId())
				.setStatus(status)
				.build();
	}
}
