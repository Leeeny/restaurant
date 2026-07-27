package ru.leeeny.ordersservice.mapper;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.leeeny.ordersservice.dto.Address;
import ru.leeeny.ordersservice.dto.CreateOrderRequest;
import ru.leeeny.ordersservice.dto.GetMenuInfoResponse;
import ru.leeeny.ordersservice.dto.MenuInfo;
import ru.leeeny.ordersservice.dto.OrderResponse;
import ru.leeeny.ordersservice.entity.MenuLineItem;
import ru.leeeny.ordersservice.entity.MenuOrder;
import ru.leeeny.ordersservice.entity.OrderStatus;
import ru.leeeny.ordersservice.exception.OrderServiceException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class OrderMapper {

	public MenuOrder mapToOrder(CreateOrderRequest request,
	                            String username,
	                            GetMenuInfoResponse response) {
		var infos = response.getMenuInfos();
		throwIfHasUnavailableMenuItems(infos);

		var menuLineItems = getMenuLineItems(request, infos);

		return MenuOrder.builder()
				.createdBy(username)
				.city(request.getAddress().getCity())
				.street(request.getAddress().getStreet())
				.house(request.getAddress().getHouse())
				.apartment(request.getAddress().getApartment())
				.totalPrice(menuLineItems
						.stream()
						.map(item ->
								item
										.getPrice()
										.multiply(BigDecimal.valueOf(item.getQuantity()))
						)
						.reduce(BigDecimal.ZERO, BigDecimal::add))
				.menuLineItems(menuLineItems)
				.status(OrderStatus.NEW) //TODO
				.createdAt(LocalDateTime.now()) //TODO
				.build();

	}

	public OrderResponse mapToResponse(MenuOrder order) {
		return OrderResponse.builder()
				.orderId(order.getId())
				.totalPrice(order.getTotalPrice())
				.menuLineItems(order.getMenuLineItems())
				.address(Address.builder()
						.city(order.getCity())
						.street(order.getStreet())
						.house(order.getHouse())
						.apartment(order.getApartment())
						.build())
				.orderStatus(order.getStatus())
				.createdAt(order.getCreatedAt())
				.build();
	}

	private void throwIfHasUnavailableMenuItems(List<MenuInfo> infos) {
		boolean hasUnavailable = infos.stream().anyMatch(m -> !m.getIsAvailable());
		if (hasUnavailable) {
			var msg = String.format("Cannot create order, because some menu items are not available: %s",
					infos);
			throw new OrderServiceException(msg, HttpStatus.NOT_FOUND);
		}
	}

	private List<MenuLineItem> getMenuLineItems(CreateOrderRequest request, List<MenuInfo> infos) {
		return infos.stream()
				.map(info -> {
					int quantity = request.getNameToQuantity().get(info.getName());
					return MenuLineItem.builder()
							.menuItemName(info.getName())
							.price(info.getPrice())
							.quantity(quantity)
							.build();
				}).toList();
	}

}
