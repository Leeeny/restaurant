package ru.leeeny.ordersservice.repository;

import io.r2dbc.spi.ConnectionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.data.r2dbc.test.autoconfigure.DataR2dbcTest;
import org.springframework.boot.jackson.autoconfigure.JacksonAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import ru.leeeny.ordersservice.TestcontainersConfiguration;
import ru.leeeny.ordersservice.config.R2dbcConfig;
import ru.leeeny.ordersservice.entity.MenuOrder;

import static ru.leeeny.ordersservice.testdata.TestConstants.ORDER_ONE_DATE;
import static ru.leeeny.ordersservice.testdata.TestConstants.ORDER_THREE_DATE;
import static ru.leeeny.ordersservice.testdata.TestConstants.ORDER_TWO_DATE;
import static ru.leeeny.ordersservice.testdata.TestConstants.USERNAME_ONE;

@Import({R2dbcConfig.class, TestcontainersConfiguration.class})
@ImportAutoConfiguration({JacksonAutoConfiguration.class})
@DataR2dbcTest
@Testcontainers
class MenuOrderRepositoryTest {

	@Autowired
	private MenuOrderRepository menuOrderRepository;

	@Autowired
	private ConnectionFactory connectionFactory;

	@BeforeEach
	void populateDb(@Value("classpath:db/insert-orders.sql") Resource script) {
		executeScriptBlocking(script);
	}

	@AfterEach
	void clearDb(@Value("classpath:db/delete-orders.sql") Resource script) {
		executeScriptBlocking(script);
	}

	@Test
	void findAllByCreatedBy_returnsCorrectSortedByDateAsc() {
		var pageRequest = PageRequest.of(0, 2)
				.withSort(Sort.by(Sort.Direction.ASC, "createdAt"));
		Flux<MenuOrder> orders = menuOrderRepository.findAllByCreatedBy(USERNAME_ONE, pageRequest);
		StepVerifier.create(orders)
				.expectNextMatches(order ->
						order.getCreatedBy().equals(USERNAME_ONE) &&
								order.getCreatedAt().equals(ORDER_ONE_DATE))
				.expectNextMatches(order ->
						order.getCreatedBy().equals(USERNAME_ONE) &&
								order.getCreatedAt().equals(ORDER_TWO_DATE))
				.verifyComplete();
	}

	@Test
	void findAllByCreatedBy_returnsEmptyListWhenUserHasNoOrders() {
		var pageRequest = PageRequest.of(0, 10)
				.withSort(Sort.by(Sort.Direction.ASC, "createdAt"));

		Flux<MenuOrder> orders = menuOrderRepository.findAllByCreatedBy("unknown user", pageRequest);
		StepVerifier.create(orders)
				.expectNextCount(0)
				.verifyComplete();
	}

	@Test
	void findAllByCreatedBy_returnsCorrectSortedByDateDesc() {
		var pageRequest = PageRequest.of(0, 2)
				.withSort(Sort.by(Sort.Direction.DESC, "createdAt"));

		Flux<MenuOrder> orders = menuOrderRepository.findAllByCreatedBy(USERNAME_ONE, pageRequest);
		StepVerifier.create(orders)
				.expectNextMatches(order ->
						order.getCreatedBy().equals(USERNAME_ONE) &&
								order.getCreatedAt().equals(ORDER_THREE_DATE))
				.expectNextMatches(order ->
						order.getCreatedBy().equals(USERNAME_ONE) &&
								order.getCreatedAt().equals(ORDER_TWO_DATE) &&
								order.getUpdatedAt() != null
				).verifyComplete();

	}

	// https://stackoverflow.com/a/73233121
	private void executeScriptBlocking(final Resource sqlScript) {
		var populator = new ResourceDatabasePopulator();
		populator.addScript(sqlScript);
		populator.populate(connectionFactory).block();
	}
}
