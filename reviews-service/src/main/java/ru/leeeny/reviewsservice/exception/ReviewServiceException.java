package ru.leeeny.reviewsservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ReviewServiceException extends RuntimeException {

	private final HttpStatus status;

	public ReviewServiceException(String message) {
		super(message);
		this.status = HttpStatus.BAD_REQUEST;
	}

	public ReviewServiceException(String message, HttpStatus cause) {
		super(message);
		this.status = cause;
	}

}
