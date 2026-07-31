package ru.leeeny.menuaggregateservice.dto.exception;

import org.springframework.http.HttpStatus;

public class MenuAggregateException extends RuntimeException {

	private HttpStatus status;

	public MenuAggregateException(String message, HttpStatus status) {
		super(message);
		this.status = status;
	}
}
