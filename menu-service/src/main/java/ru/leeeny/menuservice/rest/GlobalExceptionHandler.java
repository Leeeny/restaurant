package ru.leeeny.menuservice.rest;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.leeeny.menuservice.dto.ErrorResponse;
import ru.leeeny.menuservice.exception.MenuServiceException;

import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(MenuServiceException.class)
	public ResponseEntity<ErrorResponse> handleMenuServiceException(MenuServiceException ex) {
		log.warn("Business Error: {} [{}]", ex.getMessage(), ex.getStatus());
		return buildResponse(ex.getStatus(), ex.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
		String message = ex.getBindingResult().getFieldErrors().stream()
				.map(f -> f.getField() + ": " + f.getDefaultMessage())
				.collect(Collectors.joining("; "));
		return buildResponse(HttpStatus.BAD_REQUEST, message);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
		return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
		String message = "Invalid value for parameter '%s': %s".formatted(ex.getName(), ex.getValue());
		return buildResponse(HttpStatus.BAD_REQUEST, message);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleNotReadable(HttpMessageNotReadableException ex) {
		return buildResponse(HttpStatus.BAD_REQUEST, "Malformed JSON request body");
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
		return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleUnexpectedError(Exception ex) {
		return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
	}

	public ResponseEntity<ErrorResponse> buildResponse(HttpStatusCode status, String message) {
		ErrorResponse body = new ErrorResponse();
		body.setMessage(message);
		body.setStatus(status.value());
		return new ResponseEntity<>(body, status);
	}
}
