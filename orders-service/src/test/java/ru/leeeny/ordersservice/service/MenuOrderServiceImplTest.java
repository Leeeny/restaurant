package ru.leeeny.ordersservice.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.leeeny.ordersservice.BaseIntegrationTest;
import ru.leeeny.ordersservice.dto.OrderResponse;
import ru.leeeny.ordersservice.dto.SortBy;
import ru.leeeny.ordersservice.entity.MenuLineItem;
import ru.leeeny.ordersservice.entity.OrderStatus;
import ru.leeeny.ordersservice.service.impl.MenuOrderServiceImpl;
import ru.leeeny.ordersservice.testdata.TestConstants;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;

import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.leeeny.ordersservice.testdata.TestConstants.CITY_ONE;
import static ru.leeeny.ordersservice.testdata.TestConstants.MENU_CREATE_ONE_PRICE;
import static ru.leeeny.ordersservice.testdata.TestConstants.MENU_CREATE_ONE_QUANTITY;
import static ru.leeeny.ordersservice.testdata.TestConstants.MENU_CREATE_THREE_PRICE;
import static ru.leeeny.ordersservice.testdata.TestConstants.MENU_CREATE_THREE_QUANTITY;
import static ru.leeeny.ordersservice.testdata.TestConstants.MENU_CREATE_TWO_PRICE;
import static ru.leeeny.ordersservice.testdata.TestConstants.MENU_CREATE_TWO_QUANTITY;
import static ru.leeeny.ordersservice.testdata.TestConstants.MENU_INFO_PATH;
import static ru.leeeny.ordersservice.testdata.TestConstants.MENU_ONE;
import static ru.leeeny.ordersservice.testdata.TestConstants.MENU_THREE;
import static ru.leeeny.ordersservice.testdata.TestConstants.MENU_TWO;
import static ru.leeeny.ordersservice.testdata.TestConstants.ORDER_ONE_DATE;
import static ru.leeeny.ordersservice.testdata.TestConstants.ORDER_THREE_DATE;
import static ru.leeeny.ordersservice.testdata.TestConstants.ORDER_TWO_DATE;
import static ru.leeeny.ordersservice.testdata.TestConstants.STREET_ONE;
import static ru.leeeny.ordersservice.testdata.TestConstants.USERNAME_ONE;
import static ru.leeeny.ordersservice.testdata.TestDataProvider.createOrderRequest;
import static ru.leeeny.ordersservice.testdata.TestDataProvider.existingItems;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MenuOrderServiceImplTest extends BaseIntegrationTest {

	@Autowired
	private MenuOrderServiceImpl menuOrderService;

	@Test
	void getOrdersOfUser_returnsCorrectFluxWhenUserHasOrders() {
		Flux<OrderResponse> orders = menuOrderService.getOrdersOfUser(USERNAME_ONE, SortBy.DATE_ASC, 0, 10);
		StepVerifier.create(orders)
				.expectNextMatches(order -> assertOrder(order, ORDER_ONE_DATE))
				.expectNextMatches(order -> assertOrder(order, ORDER_TWO_DATE))
				.expectNextMatches(order -> assertOrder(order, ORDER_THREE_DATE))
				.verifyComplete();
	}

	@Test
	void createOrder_createsOrderWhenAllMenusAreAvailable() {
		prepareStubForSuccess();

		var createOrderRequest = createOrderRequest();
		var now = LocalDateTime.now().minusNanos(1000);
		Mono<OrderResponse> response = menuOrderService.createOrder(createOrderRequest, USERNAME_ONE);
		StepVerifier.create(response)
				.expectNextMatches(orderResponse -> {
					assertThat(orderResponse.getAddress()).isEqualTo(createOrderRequest.getAddress());
					assertThat(orderResponse.getTotalPrice()).isEqualTo(TestConstants.SUCCESS_TOTAL_PRICE);
					assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.NEW);
					assertThat(orderResponse.getCreatedAt()).isAfter(now);
					var menuItems = new ArrayList<>(orderResponse.getMenuLineItems());
					menuItems.sort(Comparator.comparing(MenuLineItem::getPrice));
					assertThat(menuItems)
							.map(MenuLineItem::getMenuItemName)
							.containsExactly(MENU_ONE, MENU_TWO, MENU_THREE);
					assertThat(menuItems)
							.map(MenuLineItem::getQuantity)
							.containsExactly(MENU_CREATE_ONE_QUANTITY, MENU_CREATE_TWO_QUANTITY, MENU_CREATE_THREE_QUANTITY);
					assertThat(menuItems)
							.map(MenuLineItem::getPrice)
							.containsExactly(MENU_CREATE_ONE_PRICE, MENU_CREATE_TWO_PRICE, MENU_CREATE_THREE_PRICE);
					return orderResponse.getOrderId() != null;
				})
				.verifyComplete();

		wiremock.verify(1, postRequestedFor(urlEqualTo(MENU_INFO_PATH)));
	}

	private boolean assertOrder(OrderResponse order, LocalDateTime createdAt) {
		return order.getOrderId() != null &&
				order.getAddress().getCity().equals(CITY_ONE) &&
				order.getAddress().getStreet().equals(STREET_ONE) &&
				order.getOrderStatus().equals(OrderStatus.NEW) &&
				order.getCreatedAt().equals(createdAt) &&
				order.getMenuLineItems().equals(existingItems());
	}
}
