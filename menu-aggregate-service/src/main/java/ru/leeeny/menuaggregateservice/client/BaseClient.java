package ru.leeeny.menuaggregateservice.client;

import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import ru.leeeny.menuaggregateservice.dto.exception.MenuAggregate4xxException;
import ru.leeeny.menuaggregateservice.dto.exception.MenuAggregateException;
import ru.leeeny.menuaggregateservice.props.ExternalServiceProps;

public abstract class BaseClient {

	protected final ExternalServiceProps props;

	protected BaseClient(ExternalServiceProps props) {
		this.props = props;
	}

	protected <T> Mono<T> applyTimeoutAndHandleExceptions(Mono<T> mono) {
		return mono.timeout(props.getDefaultTimeout())
				.onErrorMap(this::handleThrowable);
	}

	private Throwable handleThrowable(Throwable throwable) {
		return switch (throwable) {
			case MenuAggregateException _ -> throwable;
			case WebClientResponseException.NotFound _ ->
					new MenuAggregate4xxException(throwable.getMessage(), HttpStatus.NOT_FOUND);
			case WebClientResponseException _ ->
					new MenuAggregate4xxException(throwable.getMessage(), HttpStatus.BAD_REQUEST);
			default -> new MenuAggregateException(throwable.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		};
	}
}
