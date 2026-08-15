package ru.leeeny.ordersservice.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ru.leeeny.ordersservice.entity.OrderPlacedEvent;

public interface OrderPlacedEventRepository extends ReactiveCrudRepository<OrderPlacedEvent, Long> {

}
