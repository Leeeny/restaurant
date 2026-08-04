package ru.leeeny.menuaggregateservice.service;

import reactor.core.publisher.Mono;
import ru.leeeny.menuaggregateservice.dto.aggtegate.MenuAggregate;
import ru.leeeny.menuaggregateservice.dto.aggtegate.MenuAggregateList;
import ru.leeeny.menuaggregateservice.dto.aggtegate.RatedMenuSort;
import ru.leeeny.menuaggregateservice.dto.review.ReviewSort;

public interface AggregateService {

	Mono<MenuAggregate> getMenuAggregateInfo(Long menuId, ReviewSort sort, int from, int size);

	Mono<MenuAggregateList> getMenusWithRatings(Long categoryId, RatedMenuSort sort);
}
