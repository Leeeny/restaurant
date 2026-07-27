package ru.leeeny.ordersservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.leeeny.ordersservice.client.MenuClient;
import ru.leeeny.ordersservice.dto.CreateOrderRequest;
import ru.leeeny.ordersservice.dto.GetMenuInfoRequest;
import ru.leeeny.ordersservice.dto.OrderResponse;
import ru.leeeny.ordersservice.dto.SortBy;
import ru.leeeny.ordersservice.mapper.OrderMapper;
import ru.leeeny.ordersservice.repository.MenuOrderRepository;
import ru.leeeny.ordersservice.service.MenuOrderService;

@Service
@RequiredArgsConstructor
public class MenuOrderServiceImpl implements MenuOrderService {

	private final MenuOrderRepository menuOrderRepository;
	private final MenuClient menuClient;
	private final OrderMapper orderMapper;


	@Override
	public Mono<OrderResponse> createOrder(CreateOrderRequest createOrderRequest, String username) {
		GetMenuInfoRequest request = new GetMenuInfoRequest(createOrderRequest.getNameToQuantity().keySet());
		return menuClient.getMenuInfo(request)
				.mapNotNull(response ->
						orderMapper.mapToOrder(createOrderRequest, username, response)
				)
				.flatMap(menuOrderRepository::save)
				.map(orderMapper::mapToResponse);
	}

	@Override
	public Flux<OrderResponse> getOrdersOfUser(String username, SortBy sortBy, int from, int size) {
		PageRequest pageRequest = PageRequest.of(from, size)
				.withSort(sortBy.getSort());
		return menuOrderRepository.findAllByCreatedBy(username, pageRequest)
				.map(orderMapper::mapToResponse);
	}
}
