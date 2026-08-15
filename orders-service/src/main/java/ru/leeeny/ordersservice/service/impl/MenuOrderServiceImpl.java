package ru.leeeny.ordersservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.leeeny.ordersservice.client.MenuClient;
import ru.leeeny.ordersservice.dto.CreateOrderRequest;
import ru.leeeny.ordersservice.dto.GetMenuInfoRequest;
import ru.leeeny.ordersservice.dto.OrderResponse;
import ru.leeeny.ordersservice.dto.SortBy;
import ru.leeeny.ordersservice.exception.OrderServiceException;
import ru.leeeny.ordersservice.mapper.OrderMapper;
import ru.leeeny.ordersservice.mapper.OrderOutboxMapper;
import ru.leeeny.ordersservice.repository.MenuOrderRepository;
import ru.leeeny.ordersservice.repository.OrderPlacedEventRepository;
import ru.leeeny.ordersservice.service.MenuOrderService;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuOrderServiceImpl implements MenuOrderService {

	private final MenuOrderRepository menuOrderRepository;
	private final OrderPlacedEventRepository orderPlacedEventRepository;
	private final MenuClient menuClient;
	private final OrderMapper orderMapper;
	private final OrderOutboxMapper orderOutboxMapper;

	@Transactional
	@Override
	public Mono<OrderResponse> createOrder(CreateOrderRequest createOrderRequest, String username) {
		GetMenuInfoRequest request = new GetMenuInfoRequest(createOrderRequest.getNameToQuantity().keySet());
		return menuClient.getMenuInfo(request)
				.mapNotNull(response ->
						orderMapper.mapToOrder(createOrderRequest, username, response)
				)
				.flatMap(menuOrderRepository::save)
				.zipWhen(menuOrder -> {
					var outbox = orderOutboxMapper.toOrderOutbox(menuOrder);
					return orderPlacedEventRepository.save(outbox);
				})
				.map(tuple -> orderMapper.mapToResponse(tuple.getT1()))
				.doOnError(e -> log.error("Error saving Menu Order: {}", e.getMessage()))
				.onErrorMap(this::handleThrowable);
	}

	@Override
	public Flux<OrderResponse> getOrdersOfUser(String username, SortBy sortBy, int from, int size) {
		PageRequest pageRequest = PageRequest.of(from, size)
				.withSort(sortBy.getSort());
		return menuOrderRepository.findAllByCreatedBy(username, pageRequest)
				.map(orderMapper::mapToResponse);
	}

	private Throwable handleThrowable(Throwable t) {
		return (t instanceof OrderServiceException)
				? t
				: new OrderServiceException(t.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
