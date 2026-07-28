package ru.leeeny.ordersservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(WebExchangeBindException.class)
	public Mono<ResponseEntity<ProblemDetail>> handleValidationException(
			WebExchangeBindException ex,
			ServerHttpRequest request) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(error ->
				errors.put(error.getField(), error.getDefaultMessage())
		);
		log.error("Intercepted validation exception. Errors: {}", errors);

		var badRequest = HttpStatus.BAD_REQUEST;

		var pd = createProblemDetail(ex.getMessage(), badRequest, request);
		pd.setProperty("invalid_params", errors);

		return Mono.just(new ResponseEntity<>(pd, badRequest));
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public Mono<ResponseEntity<ProblemDetail>> handleHttpMessageNotRepeatable(
			HttpMessageNotReadableException ex,
			ServerHttpRequest request) {
		log.error("Intercepted HttpMessageNotReadableException. Message: {}", ex.getMessage());

		var badRequest = HttpStatus.BAD_REQUEST;

		var problemDetail = createProblemDetail(ex.getMessage(), badRequest, request);
		return Mono.just(new ResponseEntity<>(problemDetail, badRequest));
	}

	@ExceptionHandler(HandlerMethodValidationException.class)
	public Mono<ResponseEntity<ProblemDetail>> handleHandlerMethodValidationException(
			HandlerMethodValidationException ex,
			ServerHttpRequest request) {
		var pd = ex.getBody();
		Map<String, String> errors = new HashMap<>();
		ex.getParameterValidationResults().forEach(result ->
				result.getResolvableErrors().forEach(e ->
						errors.put(result.getMethodParameter().getParameterName(), e.getDefaultMessage())
				)
		);
		log.error("Intercepted HandlerMethodValidationException. Errors: {}", errors);
		pd.setProperty("invalid_params", errors);
		pd.setStatus(HttpStatus.BAD_REQUEST);
		pd.setInstance(request.getURI());
		return Mono.just(new ResponseEntity<>(pd, HttpStatus.BAD_REQUEST));
	}

	@ExceptionHandler(OrderServiceException.class)
	public Mono<ResponseEntity<ProblemDetail>> handleOrderServiceException(
			OrderServiceException ex,
			ServerHttpRequest request) {
		log.error("Intercepted OrderServiceException. Status: {}, Message: {}", ex.getStatus(), ex.getMessage());
		var pd = createProblemDetail(ex.getMessage(), ex.getStatus(), request);
		return Mono.just(new ResponseEntity<>(pd, ex.getStatus()));
	}

	private static ProblemDetail createProblemDetail(
			String message,
			HttpStatus httpStatus,
			ServerHttpRequest request) {
		var pd = ProblemDetail.forStatusAndDetail(httpStatus, message);
		pd.setProperty("timestamp", Instant.now());
		pd.setInstance(request.getURI());
		return pd;
	}
}
