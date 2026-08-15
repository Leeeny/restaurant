package ru.leeeny.ordersservice.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.leeeny.ordersservice.dto.CreateOrderRequest;
import ru.leeeny.ordersservice.dto.OrderResponse;
import ru.leeeny.ordersservice.dto.SortBy;
import ru.leeeny.restaurant.OrderDispatchedEvent;

public interface MenuOrderService {

	Mono<OrderResponse> createOrder(CreateOrderRequest createOrderRequest, String username);

	Flux<OrderResponse> getOrdersOfUser(String username, SortBy sortBy, int from, int size);

	Mono<Void> updateOrder(OrderDispatchedEvent event);
}
