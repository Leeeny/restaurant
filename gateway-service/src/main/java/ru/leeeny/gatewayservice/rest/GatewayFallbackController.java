package ru.leeeny.gatewayservice.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GatewayFallbackController {

	@RequestMapping("/fallback/{service}")
	public ResponseEntity<String> fallback(@PathVariable String service) {
		return ResponseEntity
				.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body(serviceName(service) + " is temporarily unavailable");
	}

	private String serviceName(String service) {
		return switch (service) {
			case "menu" -> "Menu service";
			case "orders" -> "Orders service";
			case "reviews" -> "Reviews service";
			case "menu-aggregate" -> "Menu aggregate service";
			default -> "Service";
		};
	}
}
