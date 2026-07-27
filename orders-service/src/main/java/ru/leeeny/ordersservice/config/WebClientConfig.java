package ru.leeeny.ordersservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import ru.leeeny.ordersservice.props.OrderServiceProps;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

	private final OrderServiceProps props;

	@Bean
	public WebClient webClient() {
		return WebClient.builder()
				.baseUrl(props.getMenuServiceUrl())
				.build();
	}
}
