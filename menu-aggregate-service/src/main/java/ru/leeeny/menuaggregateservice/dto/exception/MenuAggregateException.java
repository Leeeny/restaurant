package ru.leeeny.menuaggregateservice.dto.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MenuAggregateException extends RuntimeException {

	private HttpStatus status;

	public MenuAggregateException(String message, HttpStatus status) {
		super(message);
		this.status = status;
	}
}
