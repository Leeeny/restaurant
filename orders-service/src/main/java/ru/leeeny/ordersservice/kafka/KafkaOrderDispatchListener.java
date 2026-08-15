package ru.leeeny.ordersservice.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import ru.leeeny.ordersservice.service.MenuOrderService;
import ru.leeeny.restaurant.OrderDispatchedEvent;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaOrderDispatchListener {

	private final MenuOrderService menuOrderService;

	@Value("${kafkaprops.nack-sleep-duration}")
	private Duration nackSleepDuration;

	@KafkaListener(topics = {"${kafkaprops.order-dispatch-topic}"})
	public void consumeOrderDispatchEvent(OrderDispatchedEvent event,
	                                      @Header(KafkaHeaders.RECEIVED_KEY) String key,
	                                      @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
	                                      @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
	                                      Acknowledgment acknowledgment) {
		log.info("Received OrderDispatchedEvent form kafka: {}. key: {}, partition: {}, topic: {}", event, key, partition, topic);


		try {
			menuOrderService.updateOrder(event).block();

			log.info("Order DispatchedEvent with Id={} updated successfully for status: {}", event.getOrderId(), event.getStatus());

			acknowledgment.acknowledge();
		} catch (Exception e) {
			log.error("Error while processing OrderDispatchedEvent form kafka: {}, exception: {}", event, e.getMessage());
			acknowledgment.nack(nackSleepDuration);
		}

	}
}
