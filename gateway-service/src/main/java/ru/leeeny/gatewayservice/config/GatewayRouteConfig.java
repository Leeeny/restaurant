package ru.leeeny.gatewayservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.time.Duration;
import java.util.Set;
import java.util.function.Function;

import static org.springframework.cloud.gateway.server.mvc.filter.Bucket4jFilterFunctions.rateLimit;
import static org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions.circuitBreaker;
import static org.springframework.cloud.gateway.server.mvc.filter.LoadBalancerFilterFunctions.lb;
import static org.springframework.cloud.gateway.server.mvc.filter.RetryFilterFunctions.retry;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;

@Configuration
public class GatewayRouteConfig {

	private static final URI FALLBACK_URI = URI.create("forward:/fallback/menu");
	private static final String CB_NAME = "menuServiceCircuitBreaker";
	private static final String MENU_SERVICE = "MENU-SERVICE";

	private static final Function<ServerRequest, String> IP_KEY_RESOLVER = request -> request.remoteAddress()
			.map(InetSocketAddress::getAddress)
			.map(InetAddress::getHostAddress)
			.orElse("unknown");

	@Bean
	public RouterFunction<ServerResponse> menuItemsRoute() {
		return route("menu-items")
				.route(path("/v1/menu-items/**"), http())
				.filter(rateLimit(config -> config
						.setCapacity(10)
						.setPeriod(Duration.ofSeconds(1))
						.setKeyResolver(IP_KEY_RESOLVER)
				))
				.filter(lb(MENU_SERVICE))
				.filter(retry(config -> config
						.setRetries(2)
						.setMethods(Set.of(HttpMethod.GET))
						.setSeries(Set.of(HttpStatus.Series.SERVER_ERROR))
				))
				.filter(circuitBreaker(config -> config
						.setId(CB_NAME)
						.setFallbackUri(FALLBACK_URI)
						.setStatusCodes("500", "503", "504")
				))
				.build();
	}

	@Bean
	public RouterFunction<ServerResponse> menuCategoriesRoute() {
		return route("menu-categories")
				.route(path("/v1/menu-categories/**"), http())
				.filter(rateLimit(config -> config
						.setCapacity(10)
						.setPeriod(Duration.ofSeconds(1))
						.setKeyResolver(IP_KEY_RESOLVER)
				))
				.filter(lb(MENU_SERVICE))
				.filter(retry(config -> config
						.setRetries(2)
						.setMethods(Set.of(HttpMethod.GET))
						.setSeries(Set.of(HttpStatus.Series.SERVER_ERROR))
				))
				.filter(circuitBreaker(CB_NAME, FALLBACK_URI))
				.build();
	}

	@Bean
	public RouterFunction<ServerResponse> ingredientsRoute() {
		return route("ingredients")
				.route(path("/v1/ingredients/**"), http())
				.filter(rateLimit(config -> config
						.setCapacity(10)
						.setPeriod(Duration.ofSeconds(1))
						.setKeyResolver(IP_KEY_RESOLVER)
				))
				.filter(lb(MENU_SERVICE))
				.filter(retry(config -> config
						.setRetries(2)
						.setMethods(Set.of(HttpMethod.GET))
						.setSeries(Set.of(HttpStatus.Series.SERVER_ERROR))
				))
				.filter(circuitBreaker(CB_NAME, FALLBACK_URI))
				.build();
	}

	@Bean
	public RouterFunction<ServerResponse> menuInfoRoute() {
		return route("menu-info")
				.route(path("/v1/menu-info/**"), http())
				.filter(rateLimit(config -> config
						.setCapacity(10)
						.setPeriod(Duration.ofSeconds(1))
						.setKeyResolver(IP_KEY_RESOLVER)
				))
				.filter(lb(MENU_SERVICE))
				.filter(retry(config -> config
						.setRetries(2)
						.setMethods(Set.of(HttpMethod.GET))
						.setSeries(Set.of(HttpStatus.Series.SERVER_ERROR))
				))
				.filter(circuitBreaker(CB_NAME, FALLBACK_URI))
				.build();
	}

	@Bean
	public RouterFunction<ServerResponse> menuOrdersRoute() {
		return route("menu-orders")
				.route(path("/v1/menu-orders/**"), http())
				.filter(rateLimit(config -> config
						.setCapacity(10)
						.setPeriod(Duration.ofSeconds(1))
						.setKeyResolver(IP_KEY_RESOLVER)
				))
				.filter(lb("ORDERS-SERVICE"))
				.filter(retry(config -> config
						.setRetries(2)
						.setMethods(Set.of(HttpMethod.GET))
						.setSeries(Set.of(HttpStatus.Series.SERVER_ERROR))
				))
				.filter(circuitBreaker("ordersServiceCircuitBreaker", URI.create("forward:/fallback/orders")))
				.build();
	}

	@Bean
	public RouterFunction<ServerResponse> reviewsRoute() {
		return route("reviews")
				.route(path("/v1/reviews/**"), http())
				.filter(rateLimit(config -> config
						.setCapacity(10)
						.setPeriod(Duration.ofSeconds(1))
						.setKeyResolver(IP_KEY_RESOLVER)
				))
				.filter(lb("REVIEWS-SERVICE"))
				.filter(retry(config -> config
						.setRetries(2)
						.setMethods(Set.of(HttpMethod.GET))
						.setSeries(Set.of(HttpStatus.Series.SERVER_ERROR))
				))
				.filter(circuitBreaker("reviewsServiceCircuitBreaker", URI.create("forward:/fallback/reviews")))
				.build();
	}

	@Bean
	public RouterFunction<ServerResponse> menuAggregateRoute() {
		return route("menu-aggregate")
				.route(path("/v1/menu-aggregate/**"), http())
				.filter(rateLimit(config -> config
						.setCapacity(10)
						.setPeriod(Duration.ofSeconds(1))
						.setKeyResolver(IP_KEY_RESOLVER)
				))
				.filter(lb("MENU-AGGREGATE-SERVICE"))
				.filter(retry(config -> config
						.setRetries(2)
						.setMethods(Set.of(HttpMethod.GET))
						.setSeries(Set.of(HttpStatus.Series.SERVER_ERROR))
				))
				.filter(circuitBreaker(config -> config
						.setId("menuAggregateCircuitBreaker")
						.setFallbackUri("forward:/fallback/menu-aggregate")
						.setStatusCodes("500", "503", "504")
				))
				.build();
	}
}