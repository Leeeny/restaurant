package ru.leeeny.menuaggregateservice.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.leeeny.menuaggregateservice.dto.aggtegate.RatedMenuSort;
import ru.leeeny.menuaggregateservice.dto.exception.MenuAggregateException;
import ru.leeeny.menuaggregateservice.dto.menu.MenuItem;
import ru.leeeny.menuaggregateservice.dto.menu.MenuItemPageResponse;
import ru.leeeny.menuaggregateservice.props.ExternalServiceProps;

import java.util.EnumSet;
import java.util.List;

import static ru.leeeny.menuaggregateservice.dto.aggtegate.RatedMenuSort.AZ;
import static ru.leeeny.menuaggregateservice.dto.aggtegate.RatedMenuSort.PRICE_ASC;
import static ru.leeeny.menuaggregateservice.dto.aggtegate.RatedMenuSort.PRICE_DESC;
import static ru.leeeny.menuaggregateservice.dto.aggtegate.RatedMenuSort.ZA;

@Component
public class MenuClient extends BaseClient {

	public static final EnumSet<RatedMenuSort> SUPPORTED_SORTS =
			//	EnumSet.of(AZ, ZA, DATE_ASC, DATE_DESC, PRICE_ASC, PRICE_DESC);
			EnumSet.of(AZ, ZA, PRICE_ASC, PRICE_DESC);

	public static final String MENU_BACKEND = "menuBackend";

	private final WebClient webClient;

	public MenuClient(WebClient.Builder webClientBuilder, ExternalServiceProps props) {
		super(props);
		this.webClient = webClientBuilder
				.baseUrl(props.getMenuServiceUrl())
				.build();
	}

	@CircuitBreaker(name = MENU_BACKEND)
	@Retry(name = MENU_BACKEND)
	public Mono<MenuItem> getMenuItem(Long menuId) {
		var mono = webClient.get()
				.uri(uriBuilder -> uriBuilder
						.path(props.getMenuItemPath())
						.path("/{menuId}")
						.build(menuId)
				)
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.onStatus(HttpStatusCode::is5xxServerError, response ->
						Mono.error(new MenuAggregateException("Menu Service Unavailable", HttpStatus.SERVICE_UNAVAILABLE)))
				.bodyToMono(MenuItem.class);

		return applyTimeoutAndHandleExceptions(mono);
	}

	@CircuitBreaker(name = MENU_BACKEND)
	@Retry(name = MENU_BACKEND)
	public Mono<List<MenuItem>> getMenusForCategory(Long categoryId, RatedMenuSort sort) {
		var mono = webClient.get()
				.uri(uriBuilder -> uriBuilder
						.path(props.getMenuItemPath())
						.queryParam("category", categoryId)
						//	.queryParamIfPresent("sort", Optional.of(sort).filter(this::supported))
						.build()
				)
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.onStatus(HttpStatusCode::is5xxServerError, response ->
						Mono.error(new MenuAggregateException("Menus", HttpStatus.SERVICE_UNAVAILABLE)))
				.bodyToMono(MenuItemPageResponse.class)
				.map(MenuItemPageResponse::getContent);
		/*		.bodyToMono(new ParameterizedTypeReference<List<MenuItem>>() {
					// нужно чтобы не было стирания типов для jackson, здесь видно только List<???>
				});*/

		return applyTimeoutAndHandleExceptions(mono);
	}

	private boolean supported(RatedMenuSort sort) {
		return SUPPORTED_SORTS.contains(sort);
	}

}
