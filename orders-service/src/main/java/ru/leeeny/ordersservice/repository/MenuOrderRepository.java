package ru.leeeny.ordersservice.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.leeeny.ordersservice.entity.MenuOrder;
import ru.leeeny.ordersservice.entity.OrderStatus;

public interface MenuOrderRepository extends ReactiveCrudRepository<MenuOrder, Long> {

	Flux<MenuOrder> findAllByCreatedBy(String username, Pageable pageable);

	@Modifying
	@Query("UPDATE orders.orders SET status = :newState, updated_at = CURRENT_TIMESTAMP WHERE id = :orderId")
	Mono<Void> updateStatusById(Long orderId, OrderStatus newState);
}
