package ru.leeeny.ordersservice.mapper;

import org.springframework.stereotype.Component;
import ru.leeeny.ordersservice.entity.MenuOrder;
import ru.leeeny.ordersservice.entity.OrderPlacedEvent;

@Component
public class OrderOutboxMapper {

	public OrderPlacedEvent toOrderOutbox(MenuOrder order) {
		return OrderPlacedEvent.builder()
				.orderId(order.getId())
				.createdBy(order.getCreatedBy())
				.city(order.getCity())
				.street(order.getStreet())
				.house(order.getHouse())
				.apartment(order.getApartment())
				.createdAt(order.getCreatedAt())
				.build();
	}
}
