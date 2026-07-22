package ru.leeeny.ordersservice.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import ru.leeeny.ordersservice.entity.MenuOrder;

public interface MenuOrderRepository extends ReactiveCrudRepository<MenuOrder, Long> {

	Flux<MenuOrder> findAllByCreatedBy(String username, Pageable pageable);
}
